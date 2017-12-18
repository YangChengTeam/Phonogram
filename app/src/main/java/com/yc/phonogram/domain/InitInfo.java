package com.yc.phonogram.domain;

/**
 * Created by zhangkai on 2017/10/19.
 */

public class InitInfo {
    private String vipPrice;
    private String goodsPrice;
    private String alipayWay;
    private String wxpayWay;

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getAlipayWay() {
        return alipayWay;
    }

    public void setAlipayWay(String alipayWay) {
        this.alipayWay = alipayWay;
    }

    public String getWxpayWay() {
        return wxpayWay;
    }

    public void setWxpayWay(String wxpayWay) {
        this.wxpayWay = wxpayWay;
    }
}
