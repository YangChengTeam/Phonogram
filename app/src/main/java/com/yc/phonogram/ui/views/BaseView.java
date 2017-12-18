package com.yc.phonogram.ui.views;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.yc.phonogram.ui.IView;

/**
 * Created by zhangkai on 2017/7/24.
 */

public abstract class BaseView extends FrameLayout implements IView {

    protected View mRootView;

    public BaseView(Context context) {
        super(context);
        mRootView = inflate(context, getLayoutId(), this);
        init();
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRootView = inflate(context, getLayoutId(), this);
        init();
    }


    public View getView(@IdRes int id) {
        return mRootView.findViewById(id);
    }

}

