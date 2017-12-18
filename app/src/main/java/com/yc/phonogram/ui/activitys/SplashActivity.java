package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.yc.phonogram.App;
import com.yc.phonogram.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        final ImageView logoImageView = findViewById(R.id.iv_logo);
        final Integer[] bgIDs = new Integer[]{R.mipmap.splash_bg1, R.mipmap.splash_bg2, R.mipmap.splash_bg3, R.mipmap
                .splash_bg4};
        Observable.from(bgIDs).interval(300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        logoImageView.setImageDrawable(ContextCompat.getDrawable(SplashActivity.this, bgIDs[aLong.intValue() % 4]));
                        if (aLong == 4) {
                            App.getApp().getLoginInfo(new Runnable() {

                                @Override
                                public void run() {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        }
                    }
                });
    }


}
