package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/2/17.
 */

public class ShareInfo {
    private String title;

    private String url;

    private String iconUrl;

    @JSONField(name = "content")
    private String desc;

    private int share_add_hour;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getShare_add_hour() {
        return share_add_hour;
    }

    public void setShare_add_hour(int share_add_hour) {
        this.share_add_hour = share_add_hour;
    }
}
