package com.kk.pay;

import android.app.Activity;

/**
 * Created by zhangkai on 2017/3/17.
 */

public abstract class IPayAbs {
    public static final boolean Debug = false;
    public static final float Debug_Price = 0.01f;

    protected Activity mContext;


    public IPayAbs(Activity context) {
        this.mContext = context;
    }

    public abstract void pay(OrderParamsInfo orderParamsInfo,
                             IPayCallback
                                     callback);

    public float debug(float price) {
        return Debug ? Debug_Price : price;
    }
}
