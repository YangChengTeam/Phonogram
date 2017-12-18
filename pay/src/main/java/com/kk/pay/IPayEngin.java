package com.kk.pay;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;

import rx.Observable;

/**
 * Created by zhangkai on 2017/4/14.
 */

public interface IPayEngin {
    Observable<ResultInfo<OrderInfo>> pay(Context context, OrderParamsInfo orderParamsInfo);
}
