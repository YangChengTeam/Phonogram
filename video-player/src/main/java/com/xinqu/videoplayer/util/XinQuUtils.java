package com.xinqu.videoplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.xinqu.videoplayer.XinQuVideoPlayer;

import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

/**
 * Created by Nathen
 * On 2016/02/21 12:25
 */
public class XinQuUtils {

    public static boolean WIFI_TIP_DIALOG_SHOWED = false;

    public static final String TAG = "JiaoZiVideoPlayer";

    public static String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * This method requires the caller to hold the permission ACCESS_NETWORK_STATE.
     *
     * @param context context
     * @return if wifi is connected,return true
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取当前设备是否有网
     *
     * @return
     */
    public static boolean isCheckNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        int type = networkInfo.getType();
        if (type == ConnectivityManager.TYPE_MOBILE || type == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * Get activity from context object
     *
     * @param context context
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context context
     * @return AppCompatActivity if it's not null
     */
    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static void setRequestedOrientation(Context context, int orientation) {
        if (XinQuUtils.getAppCompActivity(context) != null) {
            XinQuUtils.getAppCompActivity(context).setRequestedOrientation(
                    orientation);
        } else {
            XinQuUtils.scanForActivity(context).setRequestedOrientation(
                    orientation);
        }
    }

    public static Window getWindow(Context context) {
        if (XinQuUtils.getAppCompActivity(context) != null) {
            return XinQuUtils.getAppCompActivity(context).getWindow();
        } else {
            return XinQuUtils.scanForActivity(context).getWindow();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 暂不保存播放进度
     * @param context
     * @param url
     * @return
     */
    public static void saveProgress(Context context, Object url, long progress) {
        if (!XinQuVideoPlayer.SAVE_PROGRESS) return;
        Log.i(TAG, "saveProgress: " + progress);
        if (progress < 5000) {
            progress = 0;
        }
        SharedPreferences spn = context.getSharedPreferences("JZVD_PROGRESS",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spn.edit();
        editor.putLong(url.toString(), progress);
        editor.apply();
    }

    /**
     * 暂不获取播放进度
     * @param context
     * @param url
     * @return
     */
    public static long getSavedProgress(Context context, Object url) {
        if (!XinQuVideoPlayer.SAVE_PROGRESS) return 0;
        SharedPreferences spn;
        spn = context.getSharedPreferences("JZVD_PROGRESS",
                Context.MODE_PRIVATE);
        return spn.getLong(url.toString(), 0);
    }

    /**
     * if url == null, clear all progress
     *
     * @param context context
     * @param url     if url!=null clear this url progress
     */
    public static void clearSavedProgress(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            SharedPreferences spn = context.getSharedPreferences("JZVD_PROGRESS",
                    Context.MODE_PRIVATE);
            spn.edit().clear().apply();
        } else {
            SharedPreferences spn = context.getSharedPreferences("JZVD_PROGRESS",
                    Context.MODE_PRIVATE);
            spn.edit().putInt(url, 0).apply();
        }
    }

    public static Object getCurrentFromDataSource(Object[] dataSourceObjects, int index) {
        LinkedHashMap<String, Object> map = (LinkedHashMap) dataSourceObjects[0];
        if (map != null && map.size() > 0) {
            return getValueFromLinkedMap(map, index);
        }
        return null;
    }

    public static Object getValueFromLinkedMap(LinkedHashMap<String, Object> map, int index) {
        int currentIndex = 0;
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            if (currentIndex == index) {
                return map.get(key);
            }
            currentIndex++;
        }
        return null;
    }

    public static boolean dataSourceObjectsContainsUri(Object[] dataSourceObjects, Object object) {
        LinkedHashMap<String, Object> map = (LinkedHashMap) dataSourceObjects[0];
        if (map != null) {
            return map.containsValue(object);
        }
        return false;
    }

    public static String getKeyFromDataSource(Object[] dataSourceObjects, int index) {
        LinkedHashMap<String, Object> map = (LinkedHashMap) dataSourceObjects[0];
        int currentIndex = 0;
        for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            if (currentIndex == index) {
                return key.toString();
            }
            currentIndex++;
        }
        return null;
    }
}
