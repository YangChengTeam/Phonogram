package com.yc.phonogram.domain;

/**
 * Created by suns  on 2020/4/3 20:47.
 */
public class MyOrderInfo {


    /**
     * id : 57408
     * order_sn : 202004081128236667
     * add_time : 2020-04-08 11:28:23
     * status : 0
     * money : 88.00
     * status_txt : 待支付
     */

    private String id;
    private String order_sn;
    private String add_time;
    private String status;
    private String money;
    private String status_txt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }
}
