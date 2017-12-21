package com.yc.phonogram.ui.fragments;

import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.danikula.videocache.HttpProxyCacheServer;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.adapter.ReadItemPagerAdapter;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.popupwindow.PayPopupWindow;
import com.yc.phonogram.ui.views.MainBgView;
import com.yc.phonogram.ui.widget.StrokeTextView;
import com.yc.phonogram.utils.EmptyUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

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

    private LinearLayout mProgressLayout;

    private StrokeTextView mCurrentNumberTextView;

    private KSYMediaPlayer ksyMediaPlayer;

    private boolean isPlay;

    private int readNum;

    private Integer[] bgIDs;

    private int currentPosition;

    private List<PhonogramInfo> phonogramInfos;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_to_me;
    }

    @Override
    public void init() {
        mCompositeSubscription = new CompositeSubscription();
        mainBgView = (MainBgView) getView(R.id.mainBgView);
        viewPager = (ViewPager) getView(R.id.view_pager);

        readItemPagerAdapter1 = new ReadItemPagerAdapter(getActivity(), null);
        viewPager.setAdapter(readItemPagerAdapter1);
        viewPager.setOffscreenPageLimit(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position >= 3 && !MainActivity.getMainActivity().isPhonogramVip()) {
                    mainBgView.setIndex(2);
                    viewPager.setCurrentItem(2, false);
                    PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.getMainActivity());
                    payPopupWindow.show();
                    return;
                }

                stop();
                LogUtil.msg("position--->" + position);
                mainBgView.setIndex(position);
                currentPosition = mainBgView.getIndex();
                if (phonogramInfos != null && phonogramInfos.size() > 0) {
                    phonogramInfo = phonogramInfos.get(currentPosition);
                }
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
        mCurrentNumberTextView = (StrokeTextView) getView(R.id.tv_current_number);
        if (ksyMediaPlayer == null) {
            ksyMediaPlayer = new KSYMediaPlayer.Builder(getActivity()).build();
            ksyMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }

        RxView.clicks(mReadPlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                requestPlay();
            }
        });

        ksyMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                mReadPlayImageView.setImageResource(R.drawable.read_play_selector);
                play();
                playAnimation();
            }
        });

        ksyMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                mProgressLayout.setVisibility(View.VISIBLE);
                countDownRead();
            }
        });
        ksyMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                ToastUtil.toast(getActivity(), "播放错误，请稍后重试");
                MainActivity.getMainActivity().requestPermission();
                return false;
            }
        });
    }

    private void requestPlay() {
        if (phonogramInfo != null) {
            if (!EmptyUtils.isEmpty(phonogramInfo.getVoice())) {
                try {
                    isPlay = !isPlay;
                    if (isPlay) {
                        if (ksyMediaPlayer != null) {
                            if (ksyMediaPlayer.isPlaying()) {
                                ksyMediaPlayer.stop();
                            }
                            ksyMediaPlayer.reset();
                        }

                        mReadPlayImageView.setImageResource(R.mipmap.reading_icon);

                        String proxyUrl = phonogramInfo.getVoice();
                        HttpProxyCacheServer proxy = App.getProxy();
                        if (null != proxy) {
                            proxyUrl = proxy.getProxyUrl(phonogramInfo.getVoice());
                        }
                        ksyMediaPlayer.setDataSource(proxyUrl);
                        ksyMediaPlayer.prepareAsync();

                    } else {
                        mReadPlayImageView.setImageResource(R.drawable.read_stop_selector);
                        stop();
                    }
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
        mainBgView.setIndexListener(new MainBgView.IndexListener() {
            @Override
            public void leftClick(int position) {

                if (position >= 3 && !MainActivity.getMainActivity().isPhonogramVip()) {
                    mainBgView.setIndex(2);
                    viewPager.setCurrentItem(2, false);
                    PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.getMainActivity());
                    payPopupWindow.show();
                    return;
                }

                stop();
                viewPager.setCurrentItem(position);
                currentPosition = mainBgView.getIndex();
                if (phonogramInfos != null && phonogramInfos.size() > 0) {
                    phonogramInfo = phonogramInfos.get(currentPosition);
                }
            }

            @Override
            public void rightClcik(int position) {

                if (position >= 3 && !MainActivity.getMainActivity().isPhonogramVip()) {
                    mainBgView.setIndex(2);
                    viewPager.setCurrentItem(2, false);
                    PayPopupWindow payPopupWindow = new PayPopupWindow(MainActivity.getMainActivity());
                    payPopupWindow.show();
                    return;
                }

                stop();
                viewPager.setCurrentItem(position);
                currentPosition = mainBgView.getIndex();
                if (phonogramInfos != null && phonogramInfos.size() > 0) {
                    phonogramInfo = phonogramInfos.get(currentPosition);
                }
            }
        });
        currentPosition = mainBgView.getIndex();
        if (phonogramInfos != null && phonogramInfos.size() > 0) {
            phonogramInfo = phonogramInfos.get(currentPosition);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlay = false;
        readNum = 0;
        mProgressBar.setProgress(100);
        mProgressLayout.setVisibility(View.INVISIBLE);
    }

    public void playAnimation() {
        Subscription subscription = Observable.interval(300, TimeUnit.MILLISECONDS).observeOn
                (AndroidSchedulers
                        .mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mAnimationImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), bgIDs[aLong.intValue() % 4]));
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    public void play() {
        if (readNum == 3) {
            readNum = 0;
        }
        mCurrentNumberTextView.setText((readNum + 1) + "");
        ksyMediaPlayer.start();
    }

    public void stop() {
        isPlay = false;
        readNum = 0;
        mReadPlayImageView.setImageResource(R.drawable.read_stop_selector);
        mCurrentNumberTextView.setText(readNum + "");
        mProgressBar.setProgress(100);
        mProgressLayout.setVisibility(View.INVISIBLE);
        if (ksyMediaPlayer != null) {
            ksyMediaPlayer.stop();
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

                            readNum++;
                            if (readNum < 3) {
                                play();
                            } else {
                                isPlay = false;
                                stop();
                            }
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
                ksyMediaPlayer.release();
                ksyMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setReadCurrentPosition(int position) {
        currentPosition = position;
        mainBgView.setIndex(currentPosition);
        viewPager.setCurrentItem(currentPosition);
    }
}
