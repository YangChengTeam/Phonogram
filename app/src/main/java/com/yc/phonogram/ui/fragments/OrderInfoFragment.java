package com.yc.phonogram.ui.fragments;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.loading.LoadingDialog;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.MyOrderInfo;
import com.yc.phonogram.domain.MyOrderInfoWrapper;
import com.yc.phonogram.engin.OrderListEngine;
import com.yc.phonogram.ui.activitys.MainActivity;
import com.yc.phonogram.ui.adapter.OrderInfoAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.config.HttpConfig;
import yc.com.rthttplibrary.util.ScreenUtil;

/**
 * Created by suns  on 2020/4/3 15:46.
 */
public class OrderInfoFragment extends BaseFragment {


    private ImageView ivBack;
    private OrderInfoAdapter orderInfoAdapter;
    private OrderListEngine orderListEngine;
    private int page = 1;
    private int page_size = 10;
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_info;
    }

    @Override
    public void init() {
        orderListEngine = new OrderListEngine(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        RecyclerView recyclerView = (RecyclerView) getView(R.id.recyclerView);
        ivBack = (ImageView) getView(R.id.iv_back);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        orderInfoAdapter = new OrderInfoAdapter(null);

        recyclerView.setAdapter(orderInfoAdapter);
        recyclerView.addItemDecoration(new ItemDecoration());
        initData();
        initListener();
    }

    private void initListener() {
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (getActivity() != null) {
//                    PersonCenterFragment centerFragment= new PersonCenterFragment();
                ((MainActivity) getActivity()).popFragment();
            }
        });
    }

    private void initData() {
//        List<MyOrderInfo> orderInfos = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            orderInfos.add(new MyOrderInfo());
//        }


        if (page == 1) loadingDialog.show("获取订单中...");
        orderListEngine.getOrderList(page, page_size).subscribe(new DisposableObserver<yc.com.rthttplibrary.bean.ResultInfo<MyOrderInfoWrapper>>() {
            @Override
            public void onComplete() {
                if (page == 1) loadingDialog.dismiss();
            }

            @Override
            public void onNext(yc.com.rthttplibrary.bean.ResultInfo<MyOrderInfoWrapper> listResultInfo) {
                if (listResultInfo != null) {
                    if (listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                        List<MyOrderInfo> orderList = listResultInfo.data.getOrderList();

                        setNewData(orderList);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

        });
    }

    private void setNewData(List<MyOrderInfo> orderInfos) {
        if (page == 1) {
            orderInfoAdapter.setNewData(orderInfos);
        } else {
            orderInfoAdapter.addData(orderInfos);
        }

        if (orderInfos.size() == page_size) {
            page++;
            orderInfoAdapter.loadMoreComplete();
        } else {
            orderInfoAdapter.loadMoreEnd();
        }
    }


    private class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(getActivity(), 10));
        }
    }
}
