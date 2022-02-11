package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.MyOrderInfoWrapper;
import com.yc.phonogram.helper.UserInfoHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by suns  on 2020/4/8 11:16.
 */
public class OrderListEngine extends BaseEngine {
    public OrderListEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<MyOrderInfoWrapper>> getOrderList(int page, int page_szie) {

        return request.getOrderList(UserInfoHelper.getUid(), page, page_szie, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
