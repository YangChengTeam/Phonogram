package com.yc.phonogram.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jakewharton.rxbinding.view.RxView;

import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.ReadItemPagerAdapter;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.helper.SeekBarHelper;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;
import com.yc.phonogram.ui.widget.StrokeTextView;
import com.yc.phonogram.utils.AudioFileFunc;
import com.yc.phonogram.utils.AudioRecordFunc;
import com.yc.phonogram.utils.EmptyUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class ReadToMeFragment extends BaseFragment {

    private static final int WRITE = 100;

    private MainBgView mainBgView;

    private ReadItemPagerAdapter readItemPagerAdapter1;

    private ViewPager viewPager;

    private CompositeSubscription mCompositeSubscription;

    private ProgressBar mProgressBar;

    private PhonogramInfo phonogramInfo;

    private ImageView mReadPlayImageView;

    private ImageView mAnimationImageView;

    private ImageView mUserTapeImageView;

    private LinearLayout mProgressLayout;

    private StrokeTextView mCurrentNumberTextView;

    private MediaPlayer ksyMediaPlayer;

    private boolean isPlay;

    private Integer[] bgIDs;

    private int currentPosition;

    private List<PhonogramInfo> phonogramInfos;

    private AudioRecordFunc audioRecordFunc;

    private int outNumber;

    public int playStep = 1;//播放步骤:1第一步引导语，2开始循环引导播放

    public int inStep = 0;

    Subscription subscriptionAnimation;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_to_me;
    }

    @Override
    public void init() {
        audioRecordFunc = AudioRecordFunc.getInstance();
        mCompositeSubscription = new CompositeSubscription();
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        viewPager = (ViewPager) getView(R.id.view_pager);

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        readItemPagerAdapter1 = new ReadItemPagerAdapter(getActivity(), null);
        viewPager.setAdapter(readItemPagerAdapter1);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bgIDs = new Integer[]{R.mipmap.splash_bg1, R.mipmap.splash_bg2, R.mipmap.splash_bg3, R.mipmap.splash_bg4};
        mProgressLayout = (LinearLayout) getView(R.id.layout_progress);
        mProgressBar = (ProgressBar) getView(R.id.progress_bar);
        mReadPlayImageView = (ImageView) getView(R.id.iv_read_play);
        mAnimationImageView = (ImageView) getView(R.id.iv_read_animation);
        mUserTapeImageView = (ImageView) getView(R.id.iv_user_tape);

        mCurrentNumberTextView = (StrokeTextView) getView(R.id.tv_current_number);

        if (ksyMediaPlayer == null) {
            ksyMediaPlayer = new MediaPlayer();
        }

        mainBgView.showInnerBg();

        RxView.clicks(mReadPlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mCurrentNumberTextView.setText((outNumber + 1) + "");

                isPlay = !isPlay;
                if (isPlay) {
                    playGuideFirst();
                } else {
                    inStep = 0;
                    stop();
                }
            }
        });

        //音频装载完成
        ksyMediaPlayer.setOnPreparedListener(mp -> {
            mReadPlayImageView.setClickable(true);
            mReadPlayImageView.setImageResource(R.drawable.read_play_selector);
            play();
            playAnimation();
        });

        //音频播放完成
        ksyMediaPlayer.setOnCompletionListener(mp -> {

            if (playStep == 1) {
                requestPlay();
            } else {
                if (outNumber >= 3) {
                    stop();
                    return;
                }

                switch (inStep) {
                    case 0:
                        playTapeTips();
                        break;
                    case 1:
                        if (audioRecordFunc != null) {
                            stopAnimate();
                            mUserTapeImageView.setVisibility(View.VISIBLE);
                            mProgressLayout.setVisibility(View.VISIBLE);
                            audioRecordFunc.startRecordAndFile(getActivity());
                        }
                        countDownRead();
                        break;
                    case 2:
                        loadUserVoice();
                        break;
                    case 3:
                        mCurrentNumberTextView.setText((outNumber + 1) + "");
                        playGuideAgain();
                        break;
                    case 4:
                        playStep = 1;
                        requestPlay();
                        break;
                }
            }
        });
        ksyMediaPlayer.setOnErrorListener((mp, what, extra) -> {
            mReadPlayImageView.setClickable(true);
//            ToastUtil.toast(getActivity(), "播放错误，请稍后重试");
            LogUtil.msg("error :" + what + "---" + extra);
            MainActivity.getMainActivity().requestPermission();
            return false;
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {

            if (null != readItemPagerAdapter1 && null != viewPager && viewPager.getChildCount() > 0) {
                viewPager.setCurrentItem(MainActivity.getMainActivity().getChildCureenItemIndex(), false);
            }
        }
    }

    private void stopAnimate() {
        if (subscriptionAnimation != null && !subscriptionAnimation.isUnsubscribed()) {
            subscriptionAnimation.unsubscribe();
            subscriptionAnimation = null;
        }
        mAnimationImageView.setVisibility(View.GONE);
    }


    public void playTapeTips() {
        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.stop();
            }
            ksyMediaPlayer.reset();
        }
        AssetManager assetManager = getActivity().getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = assetManager.openFd("user_tape_tips.mp3");
            ksyMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            ksyMediaPlayer.prepareAsync();
            mReadPlayImageView.setClickable(false);
            mReadPlayImageView.setImageResource(R.mipmap.reading_icon);

            inStep = 1;
            playStep = 2;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playGuideFirst() {
        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.stop();
            }
            ksyMediaPlayer.reset();
        }
        AssetManager assetManager = getActivity().getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = assetManager.openFd("guide_01.mp3");
            ksyMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            ksyMediaPlayer.prepareAsync();
            mReadPlayImageView.setClickable(false);
            mReadPlayImageView.setImageResource(R.mipmap.reading_icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playGuideAgain() {
        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.stop();
            }
            ksyMediaPlayer.reset();
        }
        AssetManager assetManager = getActivity().getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = assetManager.openFd("guide_02.mp3");
            ksyMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            ksyMediaPlayer.prepareAsync();
            mReadPlayImageView.setClickable(false);
            mReadPlayImageView.setImageResource(R.mipmap.reading_icon);
            inStep = 4;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPlay() {
        if (phonogramInfo != null) {
            if (!EmptyUtils.isEmpty(phonogramInfo.getVoice())) {
                try {
                    if (isPlay || inStep == 4) {
                        if (ksyMediaPlayer != null) {
                            if (ksyMediaPlayer.isPlaying()) {
                                ksyMediaPlayer.stop();
                            }
                            ksyMediaPlayer.reset();
                        }

                        String proxyUrl = phonogramInfo.getVoice();
                        HttpProxyCacheServer proxy = App.getProxy();
                        if (null != proxy) {
                            proxyUrl = proxy.getProxyUrl(phonogramInfo.getVoice());
                        }
                        ksyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        ksyMediaPlayer.setVolume(2.0f, 2.0f);
                        ksyMediaPlayer.setDataSource(proxyUrl);
                        ksyMediaPlayer.prepareAsync();

                    } else {
                        mReadPlayImageView.setClickable(true);
                        mReadPlayImageView.setImageResource(R.drawable.read_stop_selector);
                        stop();
                    }
                    //inStep = 1;
                    //playStep = 2;
                    inStep = 0;
                    playStep = 2;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void loadData() {
        PhonogramListInfo phonogramListInfo = MainActivity.getMainActivity().getPhonogramListInfo();
        if (phonogramListInfo == null || phonogramListInfo.getPhonogramInfos() == null || phonogramListInfo.getPhonogramInfos().size() == 0) {
            return;
        }
        phonogramInfos = phonogramListInfo.getPhonogramInfos();

        readItemPagerAdapter1.setPhonogramInfos(phonogramInfos);
        readItemPagerAdapter1.notifyDataSetChanged();

        mainBgView.showIndex(phonogramInfos.size());
        mainBgView.setIndex(0);
        mainBgView.setIndexListener(new SeekBarHelper.IndexListener() {
            @Override
            public void leftClick(int position) {
                changePage(position);
            }

            @Override
            public void rightClcik(int position) {
                changePage(position);
            }
        });
        currentPosition = mainBgView.getIndex();
        if (phonogramInfos != null && phonogramInfos.size() > 0) {
            phonogramInfo = phonogramInfos.get(currentPosition);
        }
    }

    public void changePage(int position) {
        stop();
        if (position >= 8 && !MainActivity.getMainActivity().isPhonogramVip()) {
            mainBgView.setIndex(7);
            viewPager.setCurrentItem(7, false);
            if (UserInfoHelper.isLogin(getActivity())) {
                PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.getMainActivity());
                payPopupWindow.show();
            }
            return;
        }

        currentPosition = position;
        MainActivity.getMainActivity().setChildCureenItemIndex(currentPosition);
        if (phonogramInfos != null && phonogramInfos.size() > 0) {
            phonogramInfo = phonogramInfos.get(currentPosition);
        }
        viewPager.setCurrentItem(position);
        mainBgView.setIndex(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlay = false;

        mProgressBar.setProgress(100);
        mProgressLayout.setVisibility(View.INVISIBLE);
    }

    public void playAnimation() {
        stopAnimate();
        mAnimationImageView.setVisibility(View.VISIBLE);
        subscriptionAnimation = Observable.interval(300, TimeUnit.MILLISECONDS).observeOn
                (AndroidSchedulers
                        .mainThread())
                .subscribe(aLong -> mAnimationImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), bgIDs[aLong.intValue() % 4])));
    }

    public void play() {
        ksyMediaPlayer.start();
    }

    public void playAgain() {
        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.stop();
            }
            ksyMediaPlayer.reset();
        }

        String proxyUrl = phonogramInfo.getVoice();
        HttpProxyCacheServer proxy = App.getProxy();
        if (null != proxy) {
            proxyUrl = proxy.getProxyUrl(phonogramInfo.getVoice());
        }
        ksyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        ksyMediaPlayer.setVolume(2.0f, 2.0f);
        try {
            ksyMediaPlayer.setDataSource(proxyUrl);
            ksyMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放用户录音
     */
    public void loadUserVoice() {

        if (ksyMediaPlayer == null) {
            ksyMediaPlayer = new MediaPlayer();
            ksyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        if (ksyMediaPlayer != null) {
            if (ksyMediaPlayer.isPlaying()) {
                ksyMediaPlayer.stop();
            }
            ksyMediaPlayer.reset();
        }

        try {
            if (audioRecordFunc != null) {
                LogUtil.msg("播放用户录音文件--->" + AudioFileFunc.getWavFilePath());
                ksyMediaPlayer.setDataSource(AudioFileFunc.getWavFilePath());
                ksyMediaPlayer.prepareAsync();
                inStep = 3;
                outNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        isPlay = false;
        inStep = 0;
        playStep = 1;
        outNumber = 0;

        mReadPlayImageView.setImageResource(R.drawable.read_stop_selector);
        mCurrentNumberTextView.setText(outNumber + "");
        mProgressBar.setProgress(100);
        mProgressLayout.setVisibility(View.INVISIBLE);
        if (ksyMediaPlayer != null) {
            ksyMediaPlayer.stop();
        }

        if (subscriptionAnimation != null && !subscriptionAnimation.isUnsubscribed()) {
            subscriptionAnimation.unsubscribe();
            subscriptionAnimation = null;
        }

        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    public void countDownRead() {
        final int count = 3;
        //设置0延迟，每隔一秒发送一条数据
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        LogUtil.msg("--->" + aLong);
                        int temp = (int) ((double) aLong / (double) count * 100);
                        LogUtil.msg("progress--->" + temp);
                        mProgressBar.setProgress(temp);
                        if (aLong == 0) {
                            //isPlay = false;
                            mProgressBar.setProgress(100);
                            mProgressLayout.setVisibility(View.INVISIBLE);

                            mAnimationImageView.setVisibility(View.VISIBLE);
                            mUserTapeImageView.setVisibility(View.GONE);
                            playAnimation();

                            //停止录音
                            if (inStep == 1 && audioRecordFunc != null) {
                                audioRecordFunc.stopRecordAndFile();
                            }
                            inStep = 2;
                            playAgain();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (ksyMediaPlayer != null) {
                if (ksyMediaPlayer.isPlaying()) {
                    ksyMediaPlayer.stop();
                }
                ksyMediaPlayer.reset();
                ksyMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReadCurrentPosition(int position) {
        if (position >= 8 && !MainActivity.getMainActivity().isPhonogramVip()) {
            mainBgView.setIndex(7);
            viewPager.setCurrentItem(7, false);
            if (UserInfoHelper.isLogin(getActivity())) {
                PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.getMainActivity());
                payPopupWindow.show();
            }
            return;
        }

        stop();
        currentPosition = position;
        mainBgView.setIndex(currentPosition);
        //currentPosition = mainBgView.getIndex();
        if (phonogramInfos != null && phonogramInfos.size() > 0) {
            phonogramInfo = phonogramInfos.get(currentPosition);
        }
        viewPager.setCurrentItem(currentPosition);
        (MainActivity.getMainActivity()).setChildCureenItemIndex(position);
    }
}
