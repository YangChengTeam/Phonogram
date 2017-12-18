package com.yc.phonogram.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2017/3/6 09:00.
 * 支付方式列表
 */
public class PayWayInfo {


    private String app_id;
    private String id;
    @JSONField(name = "name")
    private String payWayName;
    private String sort;
    private String status;
    @JSONField(name = "title")
    private String payWayTitle;


    @JSONField(name = "payway_title")
    private String title;
    @JSONField(name = "payway_name")
    private String name;
    @JSONField(name = "payway_account_name")
    private String account_name;

    public PayWayInfo() {
    }

    public PayWayInfo(String name) {
        this.name = name;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayWayTitle() {
        return payWayTitle;
    }

    public void setPayWayTitle(String payWayTitle) {
        this.payWayTitle = payWayTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }
}
