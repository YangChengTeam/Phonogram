package com.kk.pay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.kk.loading.LoadingDialog;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;

import java.net.URLEncoder;


/**
 * Created by wanglin  on 2017/8/30 10:45.
 */

public class IDunXingH5PayImpl extends IPayImpl {

    public IDunXingH5PayImpl(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
    }

    public IDunXingH5PayImpl(Activity context, String type) {
        super(context);
        loadingDialog = new LoadingDialog(context);
    }


    public boolean checkAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startapp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, get(orderInfo.getMessage(), "支付失败"));
            return;
        }
        PayInfo payInfo = orderInfo.getPayInfo();
        try {
            String url = payInfo.getPayurl();
            if (!TextUtils.isEmpty(payInfo.getRemarks())) {
                String remarks = payInfo.getRemarks();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(url).append("&remarks=").append(URLEncoder.encode(remarks, "gb2312"));
                url = stringBuilder.toString();
            }

            if (!checkAliPayInstalled(mContext)) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
                return;
            }
            if (orderInfo.getPayInfo()
                    .getIsOverrideUrl() > 1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            } else {
                WebPopupWindow webPopupWindow = new WebPopupWindow(mContext, url, orderInfo.getPayInfo()
                        .getIsOverrideUrl() == 1);
                webPopupWindow.show(mContext.getWindow().getDecorView().getRootView());
            }
            IPayImpl.uiPayCallback = iPayCallback;
            IPayImpl.uOrderInfo = orderInfo;
            isGen = true;
        } catch (Exception e) {
            ToastUtil.toast2(mContext, "支付渠道出错,请联系客服");
            LogUtil.msg("Exception  " + e.getMessage());
        }

    }
}
