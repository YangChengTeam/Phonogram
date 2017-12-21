package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;

import com.yc.phonogram.R;

/**
 * Created by zhangkai on 2017/12/21.
 */

public class PhonogramPopupWindow extends BasePopupWindow {
    public PhonogramPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ppw_phonogram;
    }

    @Override
    public void init() {

    }
}
