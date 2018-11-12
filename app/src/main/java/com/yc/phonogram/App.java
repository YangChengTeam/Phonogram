package com.yc.phonogram;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.danikula.videocache.HttpProxyCacheServer;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.share.UMShareImpl;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.kk.utils.PreferenceUtil;
import com.kk.utils.TaskUtil;
import com.ksyun.media.player.KSYHardwareDecodeWhiteList;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.commonsdk.UMConfigure;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.engin.LoginEngin;
import com.yc.phonogram.utils.LPUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/10/17.
 */

public class App extends MultiDexApplication {
    private static App INSTANSE;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoagal(getApplicationContext());
        INSTANSE = this;
    }

    public static App getApp() {
        return INSTANSE;
    }

    public static void initGoagal(Context context) {
        //全局信息初始化
        GoagalInfo.get().init(context);

        //设置文件唯一性 防止手机相互拷贝
        FileUtil.setUuid(GoagalInfo.get().uuid);

        //设置http默认参数
        String agent_id = "1";
        Map<String, String> params = new HashMap<>();
        if (GoagalInfo.get().channelInfo != null && GoagalInfo.get().channelInfo.agent_id != null) {
            params.put("from_id", GoagalInfo.get().channelInfo.from_id + "");
            params.put("author", GoagalInfo.get().channelInfo.author + "");
            agent_id = GoagalInfo.get().channelInfo.agent_id;
        }
        params.put("agent_id", agent_id);
        params.put("imeil", GoagalInfo.get().uuid);
        String sv = getSV();
        params.put("sv", sv);
        params.put("device_type", "2");
        if (GoagalInfo.get().packageInfo != null) {
            params.put("app_version", GoagalInfo.get().packageInfo.versionCode + "");
        }
        HttpConfig.setDefaultParams(params);

        //友盟初始化
        UMConfigure.init(context, "5a332ce6a40fa3151800009f", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,null);

        KSYHardwareDecodeWhiteList.getInstance().init(context);
        //腾迅自动更新
        Bugly.init(context, context.getString(R.string.bugly_id), false);

        //友盟分享
        UMShareImpl.Builder builder = new UMShareImpl.Builder();

        builder.setWeixin("wx2d0b6315f8d80d64", "a194bcc16fc8bf38b0bafad7b4f00a4a")
                .setQQ("1106626200", "Vpa3dJ9rkFNy5kyi")
                .setDebug(true)
                .build(context);
    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }

    private LoginDataInfo loginDataInfo;

    public LoginDataInfo getLoginDataInfo() {
        return loginDataInfo;
    }

    private LoginEngin loginEngin;

    public void getLoginInfo(final Runnable task) {
        if (loginEngin == null) {
            loginEngin = new LoginEngin(this);
        }
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                String data = PreferenceUtil.getImpl(getApplicationContext()).getString(Config.INIT_URL, "");
                if (!data.isEmpty()) {
                    try {
                        final LoginDataInfo resultInfo = JSON.parseObject(data, new TypeReference<LoginDataInfo>() {
                        }.getType());
                        if (resultInfo != null) {
                            loginDataInfo = resultInfo;
                        }

                    } catch (Exception e) {
                        LogUtil.msg("getLoginInfo本地缓存" + e);
                    }
                }
            }
        });
        loginEngin.rxGetInfo().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResultInfo<LoginDataInfo>>() {
                    @Override
                    public void call(final ResultInfo<LoginDataInfo> resultInfo) {
                        if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                            TaskUtil.getImpl().runTask(new Runnable() {
                                @Override
                                public void run() {
                                    PreferenceUtil.getImpl(getApplicationContext()).putString(Config.INIT_URL, JSON.toJSONString
                                            (resultInfo.data));
                                    loginDataInfo = resultInfo.data;
                                    if (task != null) {
                                        task.run();
                                    }
                                }
                            });
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (loginDataInfo == null) {
                            getLoginInfo(task);
                            return;
                        }

                        if (task != null) {
                            task.run();
                        }
                    }
                });
    }


    private HttpProxyCacheServer proxy;
    public static HttpProxyCacheServer getProxy() {
        App app = (App) INSTANSE.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    /**
     * 构造100M大小的缓存池
     * @return
     */
    private HttpProxyCacheServer newProxy() {
        int cacheSize = 100 * 1024 * 1024;
        String videoCacheDir = LPUtils.getInstance().getVideoCacheDir(getApplicationContext());
        //如果SD卡已挂载并且可读写
        if(null==videoCacheDir){
            return null;
        }
        //优先使用内部缓存
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(new File(videoCacheDir))
                .maxCacheSize(cacheSize)//1BG缓存大小上限
                .build();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
