package com.yc.phonogram.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.yc.phonogram.App;

/**
 * Created by wanglin  on 2019/5/17 09:52.
 */
public class SharePreferenceUtils {

    private static SharePreferenceUtils instance;
    private SharedPreferences sp;


    public static SharePreferenceUtils getInstance() {
        synchronized (SharePreferenceUtils.class) {
            if (instance == null) {
                synchronized (SharePreferenceUtils.class) {
                    instance = new SharePreferenceUtils();
                }
            }
        }
        return instance;
    }

    private SharePreferenceUtils() {
        sp = App.getApp().getSharedPreferences("phonogram", Context.MODE_PRIVATE);
    }

    public void putString(String key, String content) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, content);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public void putBoolean(String key, boolean b) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, b).apply();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

}
