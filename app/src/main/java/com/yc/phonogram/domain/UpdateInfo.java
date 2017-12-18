package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/3/7.
 */

public class UpdateInfo {
    private String version;

    @JSONField(name = "app_url")
    private String appUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    private String desp;

}
