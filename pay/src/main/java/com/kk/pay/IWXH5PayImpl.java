package com.kk.pay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;

import com.alibaba.fastjson.TypeReference;
import com.kk.loading.LoadingDialog;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * Created by zhangkai on 2017/4/26.
 */

public class IWXH5PayImpl extends IPayImpl {
    private static final String appidStr = "?app_id=1";

    public IWXH5PayImpl(Activity context) {
        super(context);

    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }
        PayInfo payInfo = orderInfo.getPayInfo();
        Map<String, String> params = new HashMap<>();
        params.put("partnerid", payInfo.getPartnerid());
        params.put("order_sn", orderInfo.getOrder_sn());
        params.put("goods_title", orderInfo.getName());
        params.put("money", orderInfo.getMoney() + "");
        params.put("front_notify_url", payInfo.getFrontnotifyurl());
        params.put("notify_url", payInfo.getNotify_url());
        params.put("starttime", payInfo.getStarttime());
        HttpCoreEngin.get(mContext).rxpost(payInfo.getPayurl() + appidStr, new TypeReference<ResultInfo<String>>() {
                }.getType(), params, true,
                true, true)
                .observeOn
                        (AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<String>>() {
            @Override
            public void call(ResultInfo<String> resultInfo) {
                if (resultInfo.code == HttpConfig.STATUS_OK) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(resultInfo.data));
                        mContext.startActivity(intent);
                        IPayImpl.uiPayCallback = iPayCallback;
                        IPayImpl.uOrderInfo = orderInfo;
                        isGen = true;
                        n = 0;
                    } catch (Exception e) {
                        ToastUtil.toast2(mContext, "支付错误[" + e + "]");
                    }
                }
            }
        });
    }



}
