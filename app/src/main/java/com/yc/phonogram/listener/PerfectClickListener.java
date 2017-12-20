package com.yc.phonogram.listener;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

/**
 * TinyHung@Outlook.com
 * 2017/12/18.
 * 避免在1秒内出发多次点击
 */

public abstract class PerfectClickListener implements OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private int id = -1;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        int mId = v.getId();
        if (id != mId) {
            id = mId;
            lastClickTime = currentTime;
            onClickView(v);
            return;
        }
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onClickView(v);
        }
    }

    protected abstract void onClickView(View v);
}
