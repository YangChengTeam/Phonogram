package com.yc.phonogram;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Build;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.FileUtil;
import com.kk.utils.TaskUtil;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.yc.phonogram.domain.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangkai on 2017/10/17.
 */

public class App extends Application {
    private static MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        //全局信息初始化
        initGoagal(getApplicationContext());
        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                //腾迅自动更新
                Bugly.init(getApplicationContext(), getResources().getString(R.string.bugly_id), false);
                try {
                    AssetManager assetManager = getAssets();
                    AssetFileDescriptor afd = assetManager.openFd("sound.mp3");
                    player = new MediaPlayer();
                    player.setDataSource(afd.getFileDescriptor(),
                            afd.getStartOffset(), afd.getLength());
                    player.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void initGoagal(Context context) {
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
    }

    public static String getSV() {
        return Build.MODEL.contains(Build.BRAND) ? Build.MODEL + " " + Build.VERSION.RELEASE : Build.BRAND + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
    }

    public static void playMp3() {
        if (player != null) {
            player.start();
        }
    }
}
