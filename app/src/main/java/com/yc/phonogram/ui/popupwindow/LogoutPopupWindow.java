package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;

import com.yc.phonogram.R;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class LogoutPopupWindow extends BasePopupWindow {
    public LogoutPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ppw_logout;
    }

    @Override
    public void init() {

    }
}
