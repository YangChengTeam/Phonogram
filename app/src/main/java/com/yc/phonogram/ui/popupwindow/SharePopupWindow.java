package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class SharePopupWindow extends BasePopupWindow {
    public SharePopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_share_view;
    }

    @Override
    public void init() {
        TextView mTvWx = (TextView) getView(R.id.tv_wx);
        TextView mTvQq = (TextView) getView(R.id.tv_qq);
        TextView mTvCircle = (TextView) getView(R.id.tv_circle);
        TextView mTvQqZone = (TextView) getView(R.id.tv_qq_zone);
        TextView mTvCancel = (TextView) getView(R.id.tv_cancel);
        RxView.clicks(mTvWx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(mTvQq).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(mTvCircle).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(mTvQqZone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
        RxView.clicks(mTvCancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }
}