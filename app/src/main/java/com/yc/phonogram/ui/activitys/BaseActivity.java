package com.yc.phonogram.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yc.phonogram.ui.IView;

/**
 * Created by zhangkai on 2017/10/31.
 */

public abstract class BaseActivity extends FragmentActivity implements IView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            // Activity was brought to front and not created,
            // Thus finishing this will get us to the last viewed activity
            finish();
            return;
        }
        setContentView(getLayoutId());
        init();
        loadData();
    }

    public void loadData() {
    }
}
