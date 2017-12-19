package com.yc.phonogram.ui.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.kk.utils.LogUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.PhonogramInfo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class ReadItemFragment extends BaseFragment {

    private ProgressBar mProgressBar;

    private PhonogramInfo phonogramInfo;

    private ImageView piImageView;

    private MediaPlayer mMediaPlayer;

    private File currentFile;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_item;
    }

    public void setPhonogramInfo(PhonogramInfo phonogramInfo) {
        this.phonogramInfo = phonogramInfo;
    }

    @Override
    public void init() {
        mProgressBar = (ProgressBar) getView(R.id.progress_bar);
        piImageView = (ImageView) getView(R.id.iv_phonetic);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (phonogramInfo != null) {
            Glide.with(this).load(phonogramInfo.getImg()).into(piImageView);

            /*if (!EmptyUtils.isEmpty(phonogramInfo.getVoice())) {
                RxUtils.getFile(getActivity(), phonogramInfo.getVoice()).observeOn
                        (AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        currentFile = file;
                        play();
                    }
                });
            }*/
        }

        countDownRead();
    }

    public void play() {
        if (currentFile != null) {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(getActivity(), Uri.parse(currentFile.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();
        } else {
            //ToastUtil.toast(getActivity(), "音频文件有误，请稍后重试");
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
                        return count - aLong; //
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
                    }
                });
    }
}
