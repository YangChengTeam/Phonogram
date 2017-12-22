package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayCallback;
import com.kk.pay.OrderInfo;
import com.kk.pay.OrderParamsInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.PreferenceUtil;
import com.kk.utils.ScreenUtil;
import com.kk.utils.TaskUtil;
import com.kk.utils.ToastUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.GoodInfo;
import com.yc.phonogram.domain.GoodListInfo;
import com.yc.phonogram.engin.GoodEngin;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.adapter.PayWayInfoAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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
    private String payway = ALI_PAY;
    private GoodEngin goodEngin;
    private GoodInfo goodInfo;


    public PayPopupWindow(Activity context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popwindow_pay_view;
    }

    @Override
    public void init() {
        goodEngin = new GoodEngin(mContext);
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

        mIvAliPay.setImageResource(R.mipmap.pay_ali_press);

        initListener();

        iPayAbs = new I1PayAbs(mContext);


    }

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
        goodEngin.getGoodList().subscribe(new Action1<ResultInfo<GoodListInfo>>() {
            @Override
            public void call(final ResultInfo<GoodListInfo> goodListInfoResultInfo) {
                if (goodListInfoResultInfo != null && goodListInfoResultInfo.code == HttpConfig.STATUS_OK
                        && goodListInfoResultInfo.data != null && goodListInfoResultInfo.data.getGoodInfoList() != null) {
                    payWayInfoAdapter.setNewData(goodListInfoResultInfo.data.getGoodInfoList());
                    goodInfo = getGoodInfo(goodListInfoResultInfo.data.getGoodInfoList());
                }
                TaskUtil.getImpl().runTask(new Runnable() {
                    @Override
                    public void run() {
                        if (goodListInfoResultInfo != null)
                            PreferenceUtil.getImpl(mContext).putString(Config.VIP_LIST_URL, JSON.toJSONString(goodListInfoResultInfo.data));
                    }
                });
            }
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
                if (goodInfo == null && MainActivity.getMainActivity().isSuperVip()) {
                    createRewardDialog();
                    return;
                }
                if (goodInfo != null) {
                    OrderParamsInfo orderParamsInfo = new OrderParamsInfo(Config.ORDER_URL, String.valueOf(goodInfo.getId()), "0", Float.parseFloat(goodInfo.getReal_price()), goodInfo.getTitle());
                    orderParamsInfo.setPayway_name(payway);

                    iPayAbs.pay(orderParamsInfo, new IPayCallback() {
                        @Override
                        public void onSuccess(OrderInfo orderInfo) {
                            MainActivity.getMainActivity().saveVip(goodInfo.getId() + "");
                            dismiss();
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

        payWayInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView mIvSelect = payWayInfoAdapter.getIv(position);
                boolean isBuy = (boolean) mIvSelect.getTag();
                if (isBuy) {
                    return;
                }

                if (preImagView == null)
                    preImagView = payWayInfoAdapter.getIv(0);

                if (preImagView != mIvSelect && !((boolean) preImagView.getTag())) {
                    preImagView.setImageResource(R.mipmap.pay_select_normal);
                }
                mIvSelect.setImageResource(R.mipmap.pay_select_press);
                preImagView = mIvSelect;
                goodInfo = payWayInfoAdapter.getItem(position);
            }
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
            goodInfo = goodInfoList.get(0);

            if (MainActivity.getMainActivity().isPhonogramVip()) {
                goodInfo = goodInfoList.get(1);
            }
            if (MainActivity.getMainActivity().isPhonicsVip()) {
                goodInfo = goodInfoList.get(0);
            }

            if ((MainActivity.getMainActivity().isPhonicsVip() && MainActivity.getMainActivity().isPhonogramVip()) || MainActivity.getMainActivity().isPhonogramOrPhonicsVip()) {
                goodInfo = goodInfoList.get(goodInfoList.size() - 1);
            }

            if (MainActivity.getMainActivity().isSuperVip()) {
                goodInfo = null;
            }
        }
        return goodInfo;
    }
}
