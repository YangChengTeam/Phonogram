package com.kk.pay;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.switfpass.pay.thread.NetHelper;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.XmlUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhangkai on 2017/6/26.
 */

public class IShiftPassAliPayImpl extends IPayImpl {

    public IShiftPassAliPayImpl(Activity context) {
        super(context);
        isGen = true;
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        final PayInfo payInfo = orderInfo.getPayInfo();
        final Map<String, String> params = new HashMap<>();
        params.put("service", "pay.alipay.native");
        params.put("mch_id", get(payInfo.getMerchantID(), "904170629809"));
        params.put("out_trade_no", orderInfo.getOrder_sn());
        params.put("body", orderInfo.getName());
        params.put("mch_create_ip", get(payInfo.getIp(), "127.0.0.1"));
        BigDecimal price = new BigDecimal((orderInfo.getMoney() * 100) + "");
        params.put("sign_type", "MD5");

        params.put("total_fee", price.intValue() + "");
        params.put("notify_url", payInfo.getNotify_url());
        params.put("nonce_str", randNonce());
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String sign = MD5.md5s(buf.toString() + "&key=" + get(payInfo.getKey(), "017e28785e1f4b3896e7e4c3fc78babc")).toUpperCase();
        params.put("sign", sign);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.msg("params  " + XmlUtils.toXml(params));
                String result = NetHelper.getInstance().HttpPost(get(payInfo.getReturn_url(), "http://pay.shiftpass.cn/gateway/"),
                        XmlUtils
                                .toXml
                                        (params));
                final HashMap mapResult = XmlUtils.parse(result);
                LogUtil.msg("result  " + result);
                if (mapResult != null && "0".equals(mapResult.get("status")) && "0".equals(mapResult.get("result_code"))) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WebPopupWindow webPopupWindow = new WebPopupWindow(mContext, mapResult.get
                                        ("code_url").toString(), orderInfo.getPayInfo().getIsOverrideUrl() == 1);
                                webPopupWindow.show(mContext.getWindow().getDecorView().getRootView());
                                IPayImpl.uiPayCallback = iPayCallback;
                                IPayImpl.uOrderInfo = orderInfo;
                                isGen = true;
                            } catch (Exception e) {
                                ToastUtil.toast2(mContext, "支付渠道出错,请联系客服");
                                LogUtil.msg("Exception  " + e.getMessage());
                            }
                        }
                    });
                } else {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.toast2(mContext, "支付渠道出错,请联系客服");
                        }
                    });
                }
            }
        }).start();


    }

    private String randNonce() {
        return new Random().nextInt(1000000000) + "";
    }

}