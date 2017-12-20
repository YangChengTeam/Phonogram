package com.yc.phonogram;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FileUtil;
import com.kk.utils.LogUtil;
import com.kk.utils.PreferenceUtil;
import com.kk.utils.TaskUtil;
import com.ksyun.media.player.KSYHardwareDecodeWhiteList;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.engin.LoginEngin;
import java.util.HashMap;
import java.util.Map;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
/**
 * Created by zhangkai on 2017/10/17.
 */

public class App extends Application {
    private static App INSTANSE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANSE = this;
        initGoagal(getApplicationContext());
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

        //动态设置渠道信息
        String appId_agentId = context.getResources().getString(R.string.app_name) + "-渠道id" + agent_id;
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(context,
                context.getResources().getString(R.string.umeng_id), appId_agentId));

        //友盟统计
        UMGameAgent.setDebugMode(Config.DEBUG);
        UMGameAgent.init(context);
        UMGameAgent.setPlayerLevel(1);
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        KSYHardwareDecodeWhiteList.getInstance().init(INSTANSE);
        //腾迅自动更新
        Bugly.init(context, context.getString(R.string.bugly_id), false);
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
}
