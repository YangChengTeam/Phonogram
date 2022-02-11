package yc.com.rthttplibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    private static PreferenceUtil instance;
    private SharedPreferences sharedPreferences;

    private PreferenceUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName() + "phonogram", Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getImpl(Context context) {
        if (instance == null) {
            synchronized (PreferenceUtil.class) {
                if (instance == null)
                    instance = new PreferenceUtil(context);
            }
        }
        return instance;
    }

    public SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public void remove(String key) {
        getEditor().remove(key).commit();
    }

    public void clearPreferences() {
        getEditor().clear().commit();
    }
}
