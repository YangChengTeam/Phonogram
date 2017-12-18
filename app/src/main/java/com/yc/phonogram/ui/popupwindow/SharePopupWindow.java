package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;

import com.yc.phonogram.R;

import dxtx.dj.pay.pay_util.bean.PuFaPayBean;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SharePopupWindow extends BasePopupWindow {
    public SharePopupWindow(Activity context) {
        super(context);
        init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_share_view;
    }

    public void init(){

    }
}