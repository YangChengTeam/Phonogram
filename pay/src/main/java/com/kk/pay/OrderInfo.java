package com.kk.pay;

import android.content.Context;

import com.alibaba.fastjson.annotation.JSONField;
import com.kk.utils.TimeUtil;

/**
 * Created by zhangkai on 2017/3/17.
 */

public class OrderInfo {
    private int viptype; //会员类型 也即商品id

    private float money; //价格 单位元

    private String name; //会员类型名 也即商品名

    private String order_sn; //订单号

    private String message;  //订单回调消息

    private String payway; //订单支付方式 原生支付宝 现代支付

    private String addtime; //订单创建时间

    private Context context; //支付上下文 用于异步回调



    @JSONField(name = "params")
    private PayInfo payInfo; //集合微信和支付宝多渠道 多sdk的 支付方式信息

    public OrderInfo(){}

    public OrderInfo( int viptype,  float money,  String
            name, String order_sn, String payway, PayInfo payInfo) {
        this.viptype = viptype;
        this.money = money;
        this.name = name;
        this.order_sn = order_sn;
        this.addtime = TimeUtil.getTimeStr2();
        this.payway = payway;
        this.payInfo = payInfo;
    }

    public int getViptype() {
        return viptype;
    }

    public void setViptype(int viptype) {
        this.viptype = viptype;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }



    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
