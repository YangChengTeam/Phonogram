package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by suns  on 2020/4/8 11:31.
 */
public class MyOrderInfoWrapper {
    @JSONField(name = "order_list")
    private List<MyOrderInfo> orderList;

    public List<MyOrderInfo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<MyOrderInfo> orderList) {
        this.orderList = orderList;
    }
}
