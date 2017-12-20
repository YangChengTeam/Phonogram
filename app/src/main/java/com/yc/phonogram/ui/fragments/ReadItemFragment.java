package com.yc.phonogram.ui.fragments;

import android.Manifest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;
import com.yc.phonogram.ui.widget.StrokeTextView;
import com.yc.phonogram.utils.EmptyUtils;
import com.yc.phonogram.utils.RxUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class ReadItemFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    private static final int WRITE = 100;

    private Subscription subscription = null;

    private ProgressBar mProgressBar;

    private PhonogramInfo phonogramInfo;

    private ImageView piImageView;

    private ImageView mReadPlayImageView;

    private ImageView mAnimationImageView;

    private LinearLayout mProgressLayout;

    private StrokeTextView mCurrentNumberTextView;

    private MediaPlayer mMediaPlayer;

    private File currentFile;

    private boolean isPlay;

    private int readNum;

    private Integer[] bgIDs;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_item;
    }

    public void setPhonogramInfo(PhonogramInfo phonogramInfo) {
        this.phonogramInfo = phonogramInfo;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(WRITE)
    private void requestPermission() {
        if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            LogUtil.msg("readitem has --->");

            if (phonogramInfo != null) {
                if (!EmptyUtils.isEmpty(phonogramInfo.getVoice())) {
                    RxUtils.getFile(getActivity(), phonogramInfo.getVoice()).observeOn
                            (AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            currentFile = file;

                            isPlay = !isPlay;
                            if (isPlay) {
                                mReadPlayImageView.setImageResource(R.mipmap.read_play_icon);
                                play();
                                playAnimation();
                            } else {
                                mReadPlayImageView.setImageResource(R.mipmap.read_stop_icon);
                                stop();
                            }
                        }
                    });
                }
            }

        } else {
            LogUtil.msg("readitem request one --->");
            // Request one permission
            EasyPermissions.requestPermissions(this, "请允许文件读写权限", WRITE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtil.msg("readitem onPermissionsGranted --->");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtil.msg("readitem onPermissionsDenied --->");
    }

    @Override
    public void init() {
        bgIDs = new Integer[]{R.mipmap.splash_bg1, R.mipmap.splash_bg2, R.mipmap.splash_bg3, R.mipmap.splash_bg4};
        mProgressLayout = (LinearLayout) getView(R.id.layout_progress);
        mProgressBar = (ProgressBar) getView(R.id.progress_bar);
        piImageView = (ImageView) getView(R.id.iv_phonetic);
        mReadPlayImageView = (ImageView) getView(R.id.iv_read_play);
        mAnimationImageView = (ImageView) getView(R.id.iv_read_animation);
        mCurrentNumberTextView = (StrokeTextView) getView(R.id.tv_current_number);
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }

        RxView.clicks(mReadPlayImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                requestPermission();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mProgressLayout.setVisibility(View.VISIBLE);
                countDownRead();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isPlay = false;
        readNum = 0;
        if (phonogramInfo != null) {
            Glide.with(this).load(phonogramInfo.getImg()).into(piImageView);
        }
    }

    public void playAnimation(){
        subscription = Observable.interval(300, TimeUnit.MILLISECONDS).observeOn
                (AndroidSchedulers
                        .mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mAnimationImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), bgIDs[aLong.intValue() % 4]));
                    }
                });
    }

    public void play() {
        if (currentFile != null) {
            if (readNum == 3) {
                readNum = 0;
            }
            mCurrentNumberTextView.setText((readNum + 1) + "");

            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(getActivity(), Uri.parse(currentFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
        } else {
            //ToastUtil.toast(getActivity(), "音频文件有误，请稍后重试");
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void countDownRead() {
        final int count = 3;
        //设置0延迟，每隔一秒发送一条数据
        Observable.interval(0, 1, TimeUnit.SECONDS)
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
                                mReadPlayImageView.setImageResource(R.mipmap.read_stop_icon);

                                stop();
                            }
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
