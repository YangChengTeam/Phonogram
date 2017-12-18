package com.kk.pay;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kk.loading.LoadingDialog;
import com.kk.utils.ToastUtil;


/**
 * Created by zhangkai on 2017/7/28.
 */

public class WebPopupWindow extends PopupWindow {
    protected Activity mContext;
    private ColorDrawable mBackgroundDrawable;

    private WebView webView;
    private ImageView ivClose;
    private LoadingDialog loadingDialog;

    public WebPopupWindow(Activity context, final String url, final boolean isOverride) {
        mContext = context;
        loadingDialog = new LoadingDialog(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contextView = inflater.inflate(R.layout.ppw_web, null);

        webView = (WebView) contextView.findViewById(R.id.wv_pay);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.4.4; zh-CN; HTC D820u Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 UCBrowser/10.1.0.527 U3/0.8.0 Mobile Safari/534.30");
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);

        if ((!Build.MANUFACTURER.toLowerCase().contains("xiaomi")) && (Build.MANUFACTURER.toLowerCase().contains("huawei"))) {

        }
        if ((Build.VERSION.SDK_INT >= 11) && (Build.MANUFACTURER.toLowerCase().contains("lenovo")))
            webView.setLayerType(1, null);

        ivClose = (ImageView) contextView.findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(contextView);
        setWindowAlpha(0.5f);
        loadingDialog.show("正在连接支付通道...");
        if (isOverride) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                    if (openWxpay(url)) {
                    } else if (openAlipay(url)) {

                    } else {
                        view.loadUrl(url);
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                    super.onPageStarted(view, url, favicon);
                    if (openWxpay(url)) {
                    } else if (openAlipay(url)) {

                    }
                }
            });
        } else {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                    super.onPageStarted(view, url, favicon);
                    if (openWxpay(url)) {
                    } else if (openAlipay(url)) {

                    }
                }
            });
        }
        webView.loadUrl(url);
    }


    private boolean openWxpay(String url) {
        if (url.startsWith("weixin://")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.toast2(mContext, "支付异常，请重试");
            }
            dismiss();
            return true;
        }
        return false;
    }


    public boolean openAlipay(String url) {
        if (url.toLowerCase().contains("platformapi/startapp")) {
            try {
                Intent intent;
                intent = Intent.parseUri(url,
                        Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                // intent.setSelector(null);
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.toast2(mContext, "支付异常，请重试");
            }
            dismiss();
            return true;

        }
        return false;
    }

    private void setWindowAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        mContext.getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        loadingDialog.dismiss();
        new MaterialDialog.Builder(mContext)
                .title("提示")
                .content("是否已经支付完成?")
                .positiveText("确定")
                .backgroundColor(Color.WHITE)
                .contentColor(Color.GRAY)
                .titleColor(Color.BLACK)
                .canceledOnTouchOutside(false)
                .keyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        }
                        return false;
                    }
                })
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (DialogAction.POSITIVE == which) {
                            if (IPayImpl.uiPayCallback != null && IPayImpl.uOrderInfo != null && IPayImpl.isGen()) {
                                IPayImpl.checkOrder(IPayImpl.uOrderInfo, IPayImpl.uiPayCallback, "http://u.wk990.com/api/index/orders_query?app_id=4");
                            }
                        }
                    }
                })
                .build().show();
        setWindowAlpha(1.f);
        super.dismiss();
    }


    @Override
    public void setContentView(View contentView) {
        if (contentView != null) {
            super.setContentView(contentView);
            setFocusable(true);
            setTouchable(true);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener(new View.OnKeyListener() {

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            dismiss();
                            return true;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    public void show(View view) {
        show(view, Gravity.CENTER);
    }

    public void show(View view, int gravity) {
        showAtLocation(view, gravity, 0, NavgationBarUtils.getNavigationBarHeight(mContext));
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);
        if (touchable) {
            if (mBackgroundDrawable == null) {
                mBackgroundDrawable = new ColorDrawable(0x00000000);
            }
            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }
}
