package com.yc.phonogram.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kk.pay.IPayImpl;
import com.kk.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by wanglin  on 2017/12/22 15:20.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, IPayImpl.appid); //appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && IPayImpl.uOrderInfo != null && IPayImpl.uiPayCallback != null) {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) //支付成功
            {
                ToastUtil.toast(WXPayEntryActivity.this, "支付成功");
                IPayImpl.uOrderInfo.setMessage("支付成功");
                IPayImpl.uiPayCallback.onSuccess(IPayImpl.uOrderInfo);
            } else if (resp.errCode == -1) // 支付错误
            {
                ToastUtil.toast(WXPayEntryActivity.this, "支付错误");

                IPayImpl.uOrderInfo.setMessage("支付错误");
                IPayImpl.uiPayCallback.onFailure(IPayImpl.uOrderInfo);

            } else if (resp.errCode == -2) // 支付取消
            {
                ToastUtil.toast(WXPayEntryActivity.this, "支付取消");
                IPayImpl.uOrderInfo.setMessage("支付取消");
                IPayImpl.uiPayCallback.onFailure(IPayImpl.uOrderInfo);

            }
            IPayImpl.uOrderInfo = null;
            IPayImpl.uiPayCallback = null;
            finish();
        }
    }
}
