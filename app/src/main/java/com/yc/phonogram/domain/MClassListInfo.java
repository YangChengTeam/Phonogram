package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class MClassListInfo {
    @JSONField(name = "list")
    private List<MClassInfo> mClassInfos;

    private GoodInfo info;

    public List<MClassInfo> getMClassInfos() {
        return mClassInfos;
    }

    public void setMClassInfos(List<MClassInfo> mClassInfos) {
        this.mClassInfos = mClassInfos;
    }

    public GoodInfo getInfo() {
        return info;
    }

    public void setInfo(GoodInfo info) {
        this.info = info;
    }
}
