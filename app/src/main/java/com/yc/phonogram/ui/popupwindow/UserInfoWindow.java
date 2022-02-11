package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.helper.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class UserInfoWindow extends BasePopupWindow {


    private RelativeLayout rlPwd;
    private RelativeLayout rlPhone;
    private ImageView ivSure;
    private boolean isNoPwd;//是否设置了密码


    public UserInfoWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_user_info_view;
    }

    @Override
    public void init() {

        setAnimationStyle(R.style.popwindow_style);

        rlPwd = (RelativeLayout) getView(R.id.rl_pwd);
        rlPhone = (RelativeLayout) getView(R.id.rl_phone);

        TextView tvPhone = (TextView) getView(R.id.tv_phone);
        TextView tvPwd = (TextView) getView(R.id.tv_pwd);
        UserInfo userInfo = UserInfoHelper.getUser();
        if (null != userInfo) {
            if (TextUtils.isEmpty(userInfo.getPwd())) {
                isNoPwd = true;
            }
        }
        tvPwd.setText(isNoPwd ? "设置密码" : "修改密码");
        ivSure = (ImageView) getView(R.id.iv_sure);

        if (null != userInfo && !TextUtils.isEmpty(userInfo.getMobile())) {
            String mobile = userInfo.getMobile();
            tvPhone.setText(mobile.replace(mobile.substring(3, 7), "****"));
        }


        initListener();
    }

    private void initListener() {

        RxView.clicks(getView(R.id.iv_pay_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

        RxView.clicks(rlPwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (isNoPwd) {
                    PasswordSetPopupWindow passwordSetPopupWindow = new PasswordSetPopupWindow(mContext);
                    passwordSetPopupWindow.show();

                } else {
                    PasswordModifyPopupWindow passwordModifyPopupWindow = new PasswordModifyPopupWindow(mContext);
                    passwordModifyPopupWindow.show();
                }
            }
        });


        RxView.clicks(ivSure).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());
    }


}
