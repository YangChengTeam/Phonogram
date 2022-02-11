package com.yc.phonogram.pay;


import android.content.Context;

import com.yc.phonogram.engin.BaseEngine;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by zhangkai on 2017/2/20.
 */

public class PayEngine extends BaseEngine {

    public PayEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<OrderInfo>> pay(OrderParamsInfo orderParamsInfo) {

        return request.pay(orderParamsInfo.getParams(), appId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
