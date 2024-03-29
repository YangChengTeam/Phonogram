package com.yc.phonogram.ui.popupwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.GoodInfo;
import com.yc.phonogram.domain.GoodListInfo;
import com.yc.phonogram.engin.GoodEngine;
import com.yc.phonogram.helper.ObservManager;
import com.yc.phonogram.pay.I1PayAbs;
import com.yc.phonogram.pay.IPayAbs;
import com.yc.phonogram.pay.IPayCallback;
import com.yc.phonogram.pay.OrderInfo;
import com.yc.phonogram.pay.OrderParamsInfo;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.adapter.PayWayInfoAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rx.functions.Action1;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.PreferenceUtil;
import yc.com.rthttplibrary.util.ScreenUtil;
import yc.com.rthttplibrary.util.TaskUtil;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class PayPopupWindow extends BasePopupWindow {

    private ImageView mIvWxPay;
    private ImageView mIvAliPay;
    private ImageButton mIvPayCharge;
    private PayWayInfoAdapter payWayInfoAdapter;
    private ImageView preImagView;
    private RecyclerView recyclerView;

    private IPayAbs iPayAbs;
    private final String WX_PAY = "wxpay";
    private final String ALI_PAY = "alipay";
    private String payway = WX_PAY;
    private GoodEngine goodEngin;

    private GoodInfo goodInfo;
    private PaySuccessTintPopWindow paySuccessTintPopWindow;

    public PayPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_pay_view;
    }

    @Override
    public void init() {

        MobclickAgent.onEvent(mContext, "open_pay_click", "打开付费界面");


        goodEngin = new GoodEngine(mContext);
        setAnimationStyle(R.style.popwindow_style);
        initData();
        mIvPayCharge = (ImageButton) getView(R.id.iv_pay_charge);
        mIvWxPay = (ImageView) getView(R.id.iv_wx_pay);
        mIvAliPay = (ImageView) getView(R.id.iv_ali_pay);
        recyclerView = (RecyclerView) getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        payWayInfoAdapter = new PayWayInfoAdapter(null);
        recyclerView.setAdapter(payWayInfoAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        mIvWxPay.setImageResource(R.mipmap.pay_wx_press);

        initListener();

        iPayAbs = new I1PayAbs(mContext);


    }

    @SuppressLint("CheckResult")
    private void initData() {

        TaskUtil.getImpl().runTask(new Runnable() {
            @Override
            public void run() {
                String str = PreferenceUtil.getImpl(mContext).getString(Config.VIP_LIST_URL, "");
                if (!TextUtils.isEmpty(str)) {
                    try {
                        final GoodListInfo goodListInfo = JSON.parseObject(str, GoodListInfo.class);
                        if (goodListInfo != null) {
                            mContext.runOnUiThread(new Runnable() {

                                @Override

                                public void run() {
                                    if (goodListInfo.getGoodInfoList() != null) {
                                        payWayInfoAdapter.setNewData(goodListInfo.getGoodInfoList());
                                        goodInfo = getGoodInfo(goodListInfo.getGoodInfoList());
                                    }

                                }
                            });
                        }
                    } catch (Exception e) {
                        LogUtil.msg("vip info read failed " + e.getMessage());
                    }

                }
            }
        });

        goodEngin.getGoodList().subscribe(goodListInfoResultInfo -> {
            if (goodListInfoResultInfo != null && goodListInfoResultInfo.code == HttpConfig.STATUS_OK
                    && goodListInfoResultInfo.data != null && goodListInfoResultInfo.data.getGoodInfoList() != null) {
                payWayInfoAdapter.setNewData(goodListInfoResultInfo.data.getGoodInfoList());
                goodInfo = getGoodInfo(goodListInfoResultInfo.data.getGoodInfoList());
            }
            TaskUtil.getImpl().runTask(() -> {
                if (goodListInfoResultInfo != null)
                    PreferenceUtil.getImpl(mContext).putString(Config.VIP_LIST_URL, JSON.toJSONString(goodListInfoResultInfo.data));
            });
        });

    }


    private void initListener() {


        RxView.clicks(getView(R.id.ll_ali_pay)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPayWay();
                mIvAliPay.setImageResource(R.mipmap.pay_ali_press);
                payway = ALI_PAY;
            }
        });
        RxView.clicks(getView(R.id.ll_wx_pay)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPayWay();
                mIvWxPay.setImageResource(R.mipmap.pay_wx_press);
                payway = WX_PAY;
            }
        });
        RxView.clicks(getView(R.id.iv_pay_close)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
        RxView.clicks(mIvPayCharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (MainActivity.getMainActivity().isPhonogramOrPhonicsVip()) {
                    createRewardDialog();
                    return;
                }

                MobclickAgent.onEvent(mContext, "pay_click", "点击充值按钮");

                if (goodInfo != null) {
                    OrderParamsInfo orderParamsInfo = new OrderParamsInfo( String.valueOf(goodInfo.getId()), "0", Float.parseFloat(goodInfo.getReal_price()), goodInfo.getTitle());
                    orderParamsInfo.setPayway_name(payway);

                    iPayAbs.pay(orderParamsInfo, new IPayCallback() {
                        @Override
                        public void onSuccess(OrderInfo orderInfo) {
                            MainActivity.getMainActivity().saveVip(goodInfo.getId() + "");
                            dismiss();
                            if (paySuccessTintPopWindow == null)
                                paySuccessTintPopWindow = new PaySuccessTintPopWindow(getContext());
                            if (!paySuccessTintPopWindow.isShowing())
                                paySuccessTintPopWindow.show();
                            ObservManager.get().notifyMyObserver("pay success");

                        }

                        @Override
                        public void onFailure(OrderInfo orderInfo) {

                        }
                    });
                } else {
                    ToastUtil.toast(mContext, HttpConfig.NET_ERROR);
                }
            }
        });

        payWayInfoAdapter.setOnItemClickListener((adapter, view, position) -> {
            ImageView mIvSelect = payWayInfoAdapter.getIv(position);
            boolean isBuy = (boolean) mIvSelect.getTag();
            if (isBuy) {
                return;
            }

            if (preImagView == null)
                preImagView = payWayInfoAdapter.getIv(getPositon());

            if (preImagView != mIvSelect && !((boolean) preImagView.getTag())) {
                preImagView.setImageResource(R.mipmap.pay_select_normal);
            }
            mIvSelect.setImageResource(R.mipmap.pay_select_press);
            preImagView = mIvSelect;
            goodInfo = payWayInfoAdapter.getItem(position);
        });

    }

    private void createRewardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("你已经购买了所有项目");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void resetPayWay() {
        mIvAliPay.setImageResource(R.mipmap.pay_ali_normal);
        mIvWxPay.setImageResource(R.mipmap.pay_wx_normal);
    }

    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(mContext, 9));
        }
    }

    private GoodInfo getGoodInfo(List<GoodInfo> goodInfoList) {
        GoodInfo goodInfo = null;
        if (goodInfoList != null && goodInfoList.size() > 0) {
            goodInfo = goodInfoList.get(getPositon());
            if (MainActivity.getMainActivity().isPhonogramOrPhonicsVip()){
                goodInfo = null;
            }

//            if (MainActivity.getMainActivity().isSuperVip() || MainActivity.getMainActivity().isCorrectPronunciation() || MainActivity.getMainActivity().isCorrectPromiss()) {
//                goodInfo = null;
//            }
//            if (MainActivity.getMainActivity().isPhonogramVip()) {
//                goodInfo = goodInfoList.get(1);
//            }
//            if (MainActivity.getMainActivity().isPhonicsVip()) {
//                goodInfo = goodInfoList.get(0);
//            }
//
//            if ((MainActivity.getMainActivity().isPhonicsVip() && MainActivity.getMainActivity().isPhonogramVip()) || MainActivity.getMainActivity().isPhonogramOrPhonicsVip()) {
//                goodInfo = goodInfoList.get(3);
//            }


        }
        return goodInfo;
    }


    private int getPositon() {
        int pos = 0;
//        if (MainActivity.getMainActivity().isPhonogramVip()) {
//            pos = 1;
//        }
//        if (MainActivity.getMainActivity().isPhonicsVip() && MainActivity.getMainActivity().isPhonogramVip() || MainActivity.getMainActivity().isPhonogramOrPhonicsVip()) {
//            pos = 3;
//        }

        return pos;
    }
}
