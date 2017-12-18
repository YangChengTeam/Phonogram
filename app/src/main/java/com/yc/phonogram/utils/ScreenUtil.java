package com.yc.phonogram.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by zhangkai on 16/9/19.
 */
public class ScreenUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @SuppressLint("NewApi")
    public static int getHeight(Context paramContext) {
        Display localDisplay = ((WindowManager) paramContext.getSystemService(Application.WINDOW_SERVICE)).getDefaultDisplay();
        Point localPoint = new Point();
        localDisplay.getSize(localPoint);
        return localPoint.y;
    }

    @SuppressLint("NewApi")
    public static int getWidth(Context paramContext) {
        Display localDisplay = ((WindowManager) paramContext.getSystemService(Application.WINDOW_SERVICE)).getDefaultDisplay();
        Point localPoint = new Point();
        localDisplay.getSize(localPoint);
        return localPoint.x;
    }

    public static int getHeight(Context context, CharSequence text, int textSize, int deviceWidth, Typeface typeface, int padding) {
        TextView textView = new TextView(context);
        textView.setPadding(padding, 0, padding, 0);
        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }


    public static  int getStatusBarHeight(Activity context) {
        Rect rectgle = new Rect();
        Window window = context.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        return rectgle.top;
    }
}
