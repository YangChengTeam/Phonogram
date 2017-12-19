package com.yc.phonogram.ui.popupwindow;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.pay.I1PayAbs;
import com.kk.pay.IPayAbs;
import com.kk.pay.IPayImpl;
import com.kk.pay.OrderParamsInfo;
import com.kk.pay.PayImplFactory;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.ScreenUtil;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.GoodListInfo;
import com.yc.phonogram.engin.GoodEngin;
import com.yc.phonogram.ui.adapter.PayWayInfoAdapter;
import com.yc.phonogram.utils.GoodItemInfoWrapper;

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
    private IPayImpl payImpl;
    private String wx_pay = "wx_pay";
    private String ali_pay = "ali_pay";
    private String payway;
    private GoodEngin goodEngin;


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
        initData();
        mIvPayCharge = (ImageView) getView(R.id.iv_pay_charge);
        mIvWxPay = (ImageView) getView(R.id.iv_wx_pay);
        mIvAliPay = (ImageView) getView(R.id.iv_ali_pay);
        recyclerView = (RecyclerView) getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        payWayInfoAdapter = new PayWayInfoAdapter(GoodItemInfoWrapper.getGoodItemInfos(mContext));
        recyclerView.setAdapter(payWayInfoAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        mIvAliPay.setImageResource(R.mipmap.pay_ali_press);

        initListener();

        iPayAbs = new I1PayAbs(mContext);

    }

    private void initData() {
        goodEngin.getGoodList().subscribe(new Action1<ResultInfo<GoodListInfo>>() {
            @Override
            public void call(ResultInfo<GoodListInfo> goodListInfoResultInfo) {

            }
        });
    }


    private void initListener() {
        RxView.clicks(getView(R.id.ll_ali_pay)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPayWay();
                mIvAliPay.setImageResource(R.mipmap.pay_ali_press);
                payway = ali_pay;
            }
        });
        RxView.clicks(getView(R.id.ll_wx_pay)).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                resetPayWay();
                mIvWxPay.setImageResource(R.mipmap.pay_wx_press);
                payway = wx_pay;
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
//                OrderParamsInfo orderParamsInfo = getOrderParamsInfo();
//                iPayAbs.pay();
            }
        });

        payWayInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImageView mIvSelect = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_select);
                if (preImagView != null) {
                    preImagView.setImageResource(R.mipmap.pay_select_normal);
                }
                mIvSelect.setImageResource(R.mipmap.pay_select_press);
                preImagView = mIvSelect;
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

    private OrderParamsInfo getOrderParamsInfo(String money, String name) {

        OrderParamsInfo orderParamsInfo = new OrderParamsInfo(Config.ORDER_URL, "0", "0", Float.parseFloat(money), name);

        return orderParamsInfo;

    }
}
