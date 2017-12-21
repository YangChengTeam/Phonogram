package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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
        RxView.clicks(getView(R.id.iv_ok)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (logoutListener != null) {
                    logoutListener.logout();
                }
            }
        });

        RxView.clicks(getView(R.id.iv_cancel)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }

    private LogoutListener logoutListener;

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    public interface LogoutListener {
        void logout();
    }
}
