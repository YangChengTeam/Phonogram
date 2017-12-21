package com.yc.phonogram.utils;

import android.os.Build;
import android.view.View;

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
}
