package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by suns  on 2020/4/4 10:48.
 */
public class UserPolicyWindow extends BasePopupWindow {


    public UserPolicyWindow(Activity context) {
        super(context);
//        this.phone = phone;
//        this.code = code;

    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_user_policy_view;
    }

    @Override
    public void init() {

        initListener();
    }

    private void initListener() {
        RxView.clicks(getView(R.id.iv_pay_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }


}
