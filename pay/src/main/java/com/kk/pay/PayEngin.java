package com.kk.pay;


import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class PayEngin implements IPayEngin {

    @Override
    public Observable<ResultInfo<OrderInfo>> pay(Context context, OrderParamsInfo orderParamsInfo) {
        return HttpCoreEngin.get(context).rxpost(orderParamsInfo.getPay_url(), new TypeReference<ResultInfo<OrderInfo>>(){}
                        .getType(),
                orderParamsInfo.getParams(),
                true,
                true, true);
    }
}
