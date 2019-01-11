package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.yc.phonogram.App;
import com.yc.phonogram.R;
import com.yc.phonogram.utils.Mp3Utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SplashActivity extends BaseActivity {
    private Subscription subscription = null;
    private MediaPlayer mediaPlayer;
    public static SplashActivity INSTANCE;
    Handler handler=new Handler();
    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        INSTANCE = this;
        final ImageView logoImageView = findViewById(R.id.iv_logo);
        final Integer[] bgIDs = new Integer[]{R.mipmap.splash_bg1, R.mipmap.splash_bg2, R.mipmap.splash_bg3, R.mipmap
                .splash_bg4};
        subscription = Observable.interval(300, TimeUnit.MILLISECONDS).observeOn
                (AndroidSchedulers
                        .mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        logoImageView.setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, bgIDs[aLong.intValue() % 4]));
                    }
                });
        mediaPlayer = Mp3Utils.playMp3(this, "splash.mp3", new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (subscription != null && !subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                    subscription = null;
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                },500);

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void finish() {
        super.finish();
        INSTANCE = null;
    }

    public static SplashActivity getInstance() {
        return INSTANCE;
    }

    @Override
    public void onBackPressed() {

    }
}
