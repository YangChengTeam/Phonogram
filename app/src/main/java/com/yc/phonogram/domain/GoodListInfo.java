package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class GoodListInfo  {
    public List<GoodInfo> getGoodInfoList() {
        return goodInfoList;
    }

    public void setGoodInfoList(List<GoodInfo> goodInfoList) {
        this.goodInfoList = goodInfoList;
    }

    @JSONField(name = "vip_list")
    private List<GoodInfo> goodInfoList;
}
