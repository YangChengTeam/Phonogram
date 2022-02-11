package com.yc.phonogram.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;

import com.yc.phonogram.App;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class UIUtils {
    public static void invoke(View view) {
        try {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.setSystemUiVisibility(view.getSystemUiVisibility() | View
                        .SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        } catch (Exception e) {
        }
    }


    public static String getVersion() {
        PackageManager manager = App.getApp().getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(App.getApp().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
