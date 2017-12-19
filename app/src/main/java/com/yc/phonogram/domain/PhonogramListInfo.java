package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class PhonogramListInfo {
    @JSONField(name = "list")
    private List<PhonogramInfo> phonogramInfos;

    public List<PhonogramInfo> getPhonogramInfos() {
        return phonogramInfos;
    }

    public void setPhonogramInfos(List<PhonogramInfo> phonogramInfos) {
        this.phonogramInfos = phonogramInfos;
    }
}
