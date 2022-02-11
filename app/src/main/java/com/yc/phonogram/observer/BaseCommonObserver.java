package com.yc.phonogram.observer;

import android.content.Context;


import com.yc.phonogram.ui.views.LoadingView;

import yc.com.rthttplibrary.converter.BaseObserver;

/**
 * Created by suns  on 2020/7/25 10:35.
 */
public abstract class BaseCommonObserver<T> extends BaseObserver<T, LoadingView> {

    public BaseCommonObserver(LoadingView view) {
        super(view);
    }

    public BaseCommonObserver(Context context) {
        this(context, "加载中...");
    }

    public BaseCommonObserver(Context context, String mess) {
        super(null);
        LoadingView loadDialog = new LoadingView(context);
        loadDialog.setText(mess);
        this.view = loadDialog;
    }

    @Override
    protected boolean isShow() {
        return false;
    }
}
