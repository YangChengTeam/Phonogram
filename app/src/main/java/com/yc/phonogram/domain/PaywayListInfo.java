package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2017/3/30 08:41.
 */

public class PaywayListInfo {
    @JSONField(name = "payway_list")
    private List<PayWayInfo> payWayInfos;

    public List<PayWayInfo> getPayWayInfos() {
        return payWayInfos;
    }

    public void setPayWayInfos(List<PayWayInfo> payWayInfos) {
        this.payWayInfos = payWayInfos;
    }
}
