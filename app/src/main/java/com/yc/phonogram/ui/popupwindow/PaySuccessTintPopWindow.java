package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.loading.LoadingDialog;
import com.yc.phonogram.R;
import com.yc.phonogram.engin.PaySuccessEngine;
import com.yc.phonogram.utils.PhoneUtils;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by wanglin  on 2018/5/15 14:40.
 */

public class PaySuccessTintPopWindow extends BasePopupWindow {

    private ImageView mIvClose;
    private EditText mEtPhone;
    private Button mBtnConfirm;
    private PaySuccessEngine paySuccessEngine;
    private LoadingDialog loadingDialog;


    public PaySuccessTintPopWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_pay_view_success;
    }

    @Override
    public void init() {
        paySuccessEngine = new PaySuccessEngine(getContext());
        loadingDialog = new LoadingDialog(getContext());
        setAnimationStyle(R.style.popwindow_style);
        mIvClose = (ImageView) getView(R.id.iv_pay_close);
        mEtPhone = (EditText) getView(R.id.et_phone);
        mBtnConfirm = (Button) getView(R.id.btn_confirm);
        initListener();
    }

    private void initListener() {

        RxView.clicks(mIvClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

        RxView.clicks(mBtnConfirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                String phone = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.toast(getContext(), "手机号不能为空");
                    return;
                }

                if (!PhoneUtils.isPhone(phone)) {
                    ToastUtil.toast(getContext(), "请输入正确的手机号");
                    return;
                }
                loadingDialog.show("正在上传手机号,请稍候...");
                paySuccessEngine.uploadPhone(phone).subscribe(stringResultInfo -> {
                    loadingDialog.dismiss();
                    dismiss();
                });
            }
        });

    }
}
