package com.yc.phonogram;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.danikula.videocache.HttpProxyCacheServer;

import com.kk.share.UMShareImpl;


import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.yc.phonogram.domain.AdvInfo;
import com.yc.phonogram.domain.AdvInfoWrapper;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.domain.UserInfoWrapper;
import com.yc.phonogram.domain.VipInfo;
import com.yc.phonogram.engin.AdvEngine;
import com.yc.phonogram.engin.LoginEngine;
import com.yc.phonogram.engin.PhoneLoginEngine;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.utils.AssetsUtil;
import com.yc.phonogram.utils.LPUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.reactivex.observers.DisposableObserver;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.GoagalInfo;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.converter.FastJsonConverterFactory;
import yc.com.rthttplibrary.request.RetrofitHttpRequest;
import yc.com.rthttplibrary.util.FileUtil;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.PreferenceUtil;
import yc.com.rthttplibrary.util.TaskUtil;
import yc.com.toutiao_adv.TTAdManagerHolder;

/**
 * Created by zhangkai on 2017/10/17.
 */

public class App extends MultiDexApplication {
    private static App INSTANSE;
    public static String privacyPolicy;

    @Override
    public void onCreate() {
        super.onCreate();
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                initGoagal(getApplicationContext());
                getAdvInfo();
                login();
            }
        });

        INSTANSE = this;
//        TTAdManagerHolder.init(this, Config.TOUTIAO_AD_ID);
        privacyPolicy = AssetsUtil.readAsset(this, "privacy_policy.txt");
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

        HttpConfig.setPublickey("-----BEGIN PUBLIC KEY-----\n" +
                "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1zQ4FOFmngBVc05sg7X5\n" +
                "Z/e3GrhG4rRAiGciUCsrd/n4wpQcKNoOeiRahxKT1FVcC6thJ/95OgBN8jaDzKdd\n" +
                "cMUti9gGzBDpGSS8MyuCOBXc6KCOYzL6Q4qnlGW2d09blZSpFUluDBBwB86yvOxk\n" +
                "5oEtnf6WPw2wiWtm7JR1JrE1k+adYfy+Cx9ifJX3wKZ5X3n+CdDXbUCPBD63eMBn\n" +
                "dy1RYOgI1Sc67bQlQGoFtrhXOGrJ8vVoRNHczaGeBOev96/V0AiEY2f5Kw5PAWhw\n" +
                "NrAF94DOLu/4OyTVUg9rDC7M97itzBSTwvJ4X5JA9TyiXL6c/77lThXvX+8m/VLi\n" +
                "mLR7PNq4e0gUCGmHCQcbfkxZVLsa4CDg2oklrT4iHvkK4ZtbNJ2M9q8lt5vgsMkb\n" +
                "bLLqe9IuTJ9O7Pemp5Ezf8++6FOeUXBQTwSHXuxBNBmZAonNZO1jACfOzm83zEE2\n" +
                "+Libcn3EBgxPnOB07bDGuvx9AoSzLjFk/T4ScuvXKEhk1xqApSvtPADrRSskV0aE\n" +
                "G5F8PfBF//krOnUsgqAgujF9unKaxMJXslAJ7kQm5xnDwn2COGd7QEnOkFwqMJxr\n" +
                "DmcluwXXaZXt78mwkSNtgorAhN6fXMiwRFtwywqoC3jYXlKvbh3WpsajsCsbTiCa\n" +
                "SBq4HbSs5+QTQvmgUTPwQikCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----");

        new RetrofitHttpRequest.Builder()
                .url(Config.getBaseUrl())
                .convert(FastJsonConverterFactory.create());

        //友盟初始化
        UMConfigure.init(context, "5a332ce6a40fa3151800009f", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);

//        KSYHardwareDecodeWhiteList.getInstance().init(context);
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

    private LoginEngine loginEngin;

    public void getLoginInfo(final Runnable task) {
        if (loginEngin == null) {
            loginEngin = new LoginEngine(this);
        }
        TaskUtil.getImpl().runTask(() -> {
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
        });

        loginEngin.rxGetInfo().subscribe(resultInfo -> {
            if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
                TaskUtil.getImpl().runTask(() -> {
                    PreferenceUtil.getImpl(getApplicationContext()).putString(Config.INIT_URL, JSON.toJSONString
                            (resultInfo.data));
                    loginDataInfo = resultInfo.data;
                    if (task != null) {
                        task.run();
                    }
                });
            }
        }, throwable -> {
            if (loginDataInfo == null) {
                getLoginInfo(task);
                return;
            }
            if (task != null) {
                task.run();
            }
        });

    }


    private PhoneLoginEngine phoneLoginEngine;

    private void login() {
        if (phoneLoginEngine == null) {
            phoneLoginEngine = new PhoneLoginEngine(this);
        }
        UserInfo userInfo = UserInfoHelper.getUser();
        if (userInfo != null) {
            String phone = userInfo.getMobile();
            String pwd = userInfo.getPwd();
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pwd))
                phoneLoginEngine.login(phone, pwd).subscribe(new DisposableObserver<ResultInfo<UserInfoWrapper>>() {

                    @Override
                    public void onNext(ResultInfo<UserInfoWrapper> userInfoWrapperResultInfo) {
                        if (userInfoWrapperResultInfo != null && userInfoWrapperResultInfo.code == HttpConfig.STATUS_OK
                                && userInfoWrapperResultInfo.data != null) {
                            UserInfo info = userInfoWrapperResultInfo.data.getUserInfo();
                            List<VipInfo> vipList = userInfoWrapperResultInfo.data.getVipList();
                            if (info != null) {
                                info.setPwd(pwd);
                            }
                            UserInfoHelper.saveUser(userInfo);
                            UserInfoHelper.saveVipList(vipList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }

                });
        }

    }


    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy() {
        App app = (App) INSTANSE.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    /**
     * 构造100M大小的缓存池
     *
     * @return
     */
    private HttpProxyCacheServer newProxy() {
        int cacheSize = 100 * 1024 * 1024;
        String videoCacheDir = LPUtils.getInstance().getVideoCacheDir(getApplicationContext());
        //如果SD卡已挂载并且可读写
        if (null == videoCacheDir) {
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

    private AdvEngine advEngine;

    private void getAdvInfo() {
        if (advEngine == null)
            advEngine = new AdvEngine(this);
        advEngine.getAdvInfo().subscribe(new DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<AdvInfoWrapper>>() {


            @Override
            public void onNext(yc.com.rthttplibrary.bean.ResultInfo<AdvInfoWrapper> advInfoWrapperResultInfo) {
                if (advInfoWrapperResultInfo != null && advInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && advInfoWrapperResultInfo.data != null) {
                    AdvInfo h5page = advInfoWrapperResultInfo.data.getH5page();
                    PreferenceUtil.getImpl(App.this).putString(Config.ADV_INFO, JSON.toJSONString(h5page));
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }


        });
    }




}
