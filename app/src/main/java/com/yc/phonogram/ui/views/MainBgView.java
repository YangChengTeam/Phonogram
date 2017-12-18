package com.yc.phonogram.ui.views;

import android.content.Context;
import android.util.AttributeSet;

import com.yc.phonogram.R;

/**
 * Created by zhangkai on 2017/12/18.
 */

public class MainBgView extends BaseView {
    public MainBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainBgView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_main_bg;
    }

    @Override
    public void init() {

    }
}
