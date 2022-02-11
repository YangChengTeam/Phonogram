package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.loading.LoadingDialog;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.engin.PhoneLoginEngine;
import com.yc.phonogram.helper.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.observers.DisposableObserver;
import rx.functions.Action1;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2020/4/4 10:48.
 */
public class PasswordSetPopupWindow extends BasePopupWindow {
    private String phone;
    private String code;
    private PhoneLoginEngine loginEngine;
    private EditText etPwd;
    private ImageView ivLogin;
    private LoadingDialog loadingDialog;


    public PasswordSetPopupWindow(Activity context) {
        super(context);
//        this.phone = phone;
//        this.code = code;
        loginEngine = new PhoneLoginEngine(context);
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_set_psd_view;
    }

    @Override
    public void init() {
        etPwd = (EditText) getView(R.id.et_pwd);
        ivLogin = (ImageView) getView(R.id.iv_login);
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            String pwd = etPwd.getText().toString().trim();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.toast(mContext, "密码不能为空");
                return;
            }
//            register(pwd);
            setPwd(pwd);

        });
        RxView.clicks(getView(R.id.iv_pay_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }


    private void setPwd(String pwd) {
        loadingDialog.show("设置密码中...");
        loginEngine.setPwd(pwd).subscribe(new DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<String>>() {
            @Override
            public void onComplete() {
                loadingDialog.dismiss();
            }

            @Override
            public void onNext(yc.com.rthttplibrary.bean.ResultInfo<String> stringResultInfo) {
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                        UserInfo userInfo = UserInfoHelper.getUser();
                        if (null != userInfo) {
                            userInfo.setPwd(pwd);
                        }
                        UserInfoHelper.saveUser(userInfo);
                        ToastUtil.toast(mContext, stringResultInfo.message);
                        dismiss();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }


        });

    }

//    private void register(String pwd) {
//        loginEngine.register(phone, code, pwd).subscribe(new Subscriber<ResultInfo<UserInfoWrapper>>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(ResultInfo<UserInfoWrapper> userInfoResultInfo) {
//                if (userInfoResultInfo != null) {
//                    if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
//                        UserInfo userInfo = userInfoResultInfo.data.getUserInfo();
//                        if (null != userInfo)
//                            userInfo.setPwd(pwd);
//                        UserInfoHelper.saveUser(userInfo);
//                        dismiss();
//                    } else {
//                        ToastUtil.toast(mContext, userInfoResultInfo.message);
//                    }
//                }
//            }
//        });
//    }
}
