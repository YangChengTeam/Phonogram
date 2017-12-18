package com.kk.pay;

/**
 * Created by zhangkai on 2017/3/17.
 */

public interface IPayCallback {
    void onSuccess(OrderInfo orderInfo);
    void onFailure(OrderInfo orderInfo);
}
