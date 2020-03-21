package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.utils.Mp3Utils;
import com.yc.phonogram.utils.VipUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.OnAdvStateListener;
import yc.com.toutiao_adv.BuildConfig;
import yc.com.toutiao_adv.TTAdDispatchManager;
import yc.com.toutiao_adv.TTAdType;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SplashActivity extends BaseActivity implements OnAdvStateListener, yc.com.toutiao_adv.OnAdvStateListener {
    private Subscription subscription = null;
    private MediaPlayer mediaPlayer;
    public static SplashActivity INSTANCE;
    Handler handler = new Handler();

    private FrameLayout container;
    private TextView skipView;
    private ImageView logoImageView;
    private RelativeLayout relativeLayout;
    private long minSplashTimeWhenNoAD = 2000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void init() {
        INSTANCE = this;
        logoImageView = findViewById(R.id.iv_logo);
        container = findViewById(R.id.splash_container);
        skipView = findViewById(R.id.skip_view);
        relativeLayout = findViewById(R.id.retry_layout);

//        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, container, skipView, Config.AV_APPID, Config.AV_SPLASH_ID, this);

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

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    }
//                }, 500);

            }
        });

        String brand = Build.BRAND.toLowerCase();
        if (VipUtils.getInstance(this).isVip()|| TextUtils.equals("huawei",brand)||TextUtils.equals("honor",brand)) {

            container.setVisibility(View.GONE);
            switchMain(minSplashTimeWhenNoAD);
        } else {
            container.setVisibility(View.VISIBLE);
            TTAdDispatchManager.getManager().init(this, TTAdType.SPLASH, container, Config.TOUTIAO_SPLASH_ID, 0, null, 0, null, 0, this);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void switchMain(long delay) {
        long delayTime = 0;
        if (delay < minSplashTimeWhenNoAD) {
            delayTime = minSplashTimeWhenNoAD - delay;
        }

        // 计算出还需要延时多久
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMain();
            }
        }, delayTime);

    }

    private void gotoMain() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        canJump = false;
//        AdvDispatchManager.getManager().onPause();
        TTAdDispatchManager.getManager().onStop();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();

            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        AdvDispatchManager.getManager().onResume();
        TTAdDispatchManager.getManager().onResume();

    }

    @Override
    public void finish() {
        super.finish();
        INSTANCE = null;
    }

    public static SplashActivity getInstance() {
        return INSTANCE;
    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onShow() {
        relativeLayout.setVisibility(View.GONE); // 广告展示后一定要把预设的开屏图片隐藏起来
        skipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(long delayTime) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        switchMain(delayTime);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }


    @Override
    public void loadSuccess() {
        switchMain(0);
    }

    @Override
    public void loadFailed() {
        switchMain(0);
    }

    @Override
    public void clickAD() {
        switchMain(0);
    }

    @Override
    public void onTTNativeExpressed(List<TTNativeExpressAd> ads) {

    }
}
