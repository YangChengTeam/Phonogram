package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/12/15.
 */

public class PayPopupWindow extends BasePopupWindow {

    private ImageView mIvWxPay;
    private ImageView mIvAliPay;
    private ImageView mIvPayCharge;
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
        mIvPayCharge = (ImageView) getView(R.id.iv_pay_charge);
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
                                        goodInfo = goodListInfo.getGoodInfoList().get(0);
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
                    goodInfo = goodListInfoResultInfo.data.getGoodInfoList().get(0);
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
                if (MainActivity.getMainActivity().isVip(goodInfo.getId() + "")) {
                    ToastUtil.toast(mContext, "你已经购买了该项目，请选择其他项目");
                    return;
                }

                OrderParamsInfo orderParamsInfo = new OrderParamsInfo(Config.ORDER_URL, String.valueOf(goodInfo.getId()), "0", Float.parseFloat(goodInfo.getReal_price()), goodInfo.getTitle());
                orderParamsInfo.setPayway_name(payway);

                iPayAbs.pay(orderParamsInfo, new IPayCallback() {
                    @Override
                    public void onSuccess(OrderInfo orderInfo) {
                        MainActivity.getMainActivity().saveVip(goodInfo.getId() + "");
                    }

                    @Override
                    public void onFailure(OrderInfo orderInfo) {

                    }
                });
            }
        });

        payWayInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView mIvSelect = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_select);
                boolean isBuy = (boolean) mIvSelect.getTag();
                if (isBuy) {
                    return;
                }

                if (preImagView == null)
                    preImagView = (ImageView) adapter.getViewByPosition(recyclerView, 0, R.id.iv_select);

                if (preImagView != mIvSelect && !((boolean) preImagView.getTag())) {
                    preImagView.setImageResource(R.mipmap.pay_select_normal);
                }
                mIvSelect.setImageResource(R.mipmap.pay_select_press);
                preImagView = mIvSelect;
                goodInfo = payWayInfoAdapter.getItem(position);
            }
        });

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
}
