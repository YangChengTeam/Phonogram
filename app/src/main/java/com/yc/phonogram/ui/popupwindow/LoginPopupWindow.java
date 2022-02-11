package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.loading.LoadingDialog;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.domain.UserInfoWrapper;
import com.yc.phonogram.domain.VipInfo;
import com.yc.phonogram.engin.PhoneLoginEngine;
import com.yc.phonogram.helper.SharePreferenceUtils;
import com.yc.phonogram.helper.UserInfoHelper;
import com.yc.phonogram.utils.PhoneUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.DisposableObserver;
import rx.functions.Action1;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2020/4/4 10:48.
 */
public class LoginPopupWindow extends BasePopupWindow {
    private PhoneLoginEngine loginEngine;
    private LoadingDialog loadingDialog;
    private EditText etPhone;
    private EditText etPwd;

    private ImageView ivLogin;
    private TextView tvCodeRegister;
    private TextView tvRegister;
    private TextView tvUserPolicy;

    public LoginPopupWindow(Activity context) {
        super(context);
        loginEngine = new PhoneLoginEngine(context);
        loadingDialog = new LoadingDialog(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_login_view;
    }

    @Override
    public void init() {
        etPhone = (EditText) getView(R.id.et_phone);
        etPwd = (EditText) getView(R.id.et_pwd);

        ivLogin = (ImageView) getView(R.id.iv_login);
        tvCodeRegister = (TextView) getView(R.id.tv_code_register);
        tvRegister = (TextView) getView(R.id.tv_register);
        tvCodeRegister.setText(Html.fromHtml("忘记了？<font color='#b05739'> 验证码登录</font>"));
        tvUserPolicy = (TextView) getView(R.id.tv_user_policy);
        tvUserPolicy.setText(Html.fromHtml("登录即代表同意<font color='#b05739'>《用户协议》</font>"));
        String phone = SharePreferenceUtils.getInstance().getString(Config.USER_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            etPhone.setText(phone);
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
        RxView.clicks(ivLogin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            String phone = etPhone.getText().toString().trim();
            String pwd = etPwd.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.toast(mContext, "手机号不能为空");
                return;
            }
            if (!PhoneUtils.isPhone(phone)) {
                ToastUtil.toast(mContext, "手机号格式不正确");
                return;
            }

            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.toast(mContext, "密码不能为空");
                return;
            }
            login(phone, pwd);

        });
        RxView.clicks(tvCodeRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                RegisterPopupWindow registerPopupWindow = new RegisterPopupWindow(mContext);
                registerPopupWindow.setLogin(true);
                registerPopupWindow.show();
                dismiss();
            }
        });

        RxView.clicks(tvRegister).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                RegisterPopupWindow registerPopupWindow = new RegisterPopupWindow(mContext);
                registerPopupWindow.show();
                dismiss();
            }
        });
        RxView.clicks(tvUserPolicy).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserPolicyWindow userPolicyWindow = new UserPolicyWindow(mContext);
                userPolicyWindow.show();
            }
        });

    }

    private void login(String phone, String pwd) {
        loadingDialog.show("登录中...");
        loginEngine.login(phone, pwd).subscribe(new DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<UserInfoWrapper>>() {

            @Override
            public void onNext(yc.com.rthttplibrary.bean.ResultInfo<UserInfoWrapper> userInfoResultInfo) {
                if (userInfoResultInfo != null) {
                    int code = userInfoResultInfo.code;
                    if (code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                        UserInfo userInfo = userInfoResultInfo.data.getUserInfo();
                        List<VipInfo> vipList = userInfoResultInfo.data.getVipList();
                        if (null != userInfo)
                            userInfo.setPwd(pwd);
                        UserInfoHelper.saveUser(userInfo);
                        UserInfoHelper.saveVipList(vipList);
                        dismiss();

                    } else {
                        if (code == 2 || userInfoResultInfo.message.contains("账号不存在")) {

                            RegisterPopupWindow registerPopupWindow = new RegisterPopupWindow(mContext);
                            registerPopupWindow.show();
                            dismiss();
                        } else {
                            ToastUtil.toast(mContext, userInfoResultInfo.message);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                loadingDialog.dismiss();
            }
        });
    }


}
