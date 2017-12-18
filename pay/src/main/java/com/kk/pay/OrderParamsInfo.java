package com.kk.pay;

import java.util.HashMap;

/**
 * Created by zhangkai on 2017/4/14.
 */

public class OrderParamsInfo {
    private String pay_url = ""; // 支付请求url
    private String goods_id = ""; //商品id 也即会员id
    private String goods_num = "1";  //商品数量
    private String type = ""; //商品购买类型

    private String payway_name = ""; //支付方式
    private String is_payway_split = "1";  //支付方式 划分标识

    private String md5signstr= ""; //现代支付 md5校验值

    private float price =  0f;
    private String name = "";

    public OrderParamsInfo(String pay_url, String goods_id, String type, float price , String name){
        this.pay_url = pay_url;
        this.goods_id = goods_id;
        this.type = type;
        this.price = price;
        this.name = name;
    }

    public String getPay_url() {
        return pay_url;
    }

    public void setPay_url(String pay_url) {
        this.pay_url = pay_url;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayway_name() {
        return payway_name;
    }

    public void setPayway_name(String payway_name) {
        this.payway_name = payway_name;
    }

    public String getIs_payway_split() {
        return is_payway_split;
    }

    public void setIs_payway_split(String is_payway_split) {
        this.is_payway_split = is_payway_split;
    }

    public String getMd5signstr() {
        return md5signstr;
    }

    public void setMd5signstr(String md5signstr) {
        this.md5signstr = md5signstr;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String dsMoney;

    public String getDsMoney() {
        return dsMoney;
    }

    public void setDsMoney(String dsMoney) {
        this.dsMoney = dsMoney;
    }

    public HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("goods_id", this.getGoods_id());
        params.put("goods_num", this.getGoods_num());
        params.put("is_payway_split", this.getIs_payway_split());
        params.put("payway_name", this.getPayway_name());
        params.put("money", this.getPrice()+"");
        params.put("ds_money", this.getDsMoney());
        params.put("type", this.getType());
        if(md5signstr != null && !md5signstr.isEmpty() && !md5signstr.equalsIgnoreCase("null")) {
            params.put("md5signstr", this.getMd5signstr());
        }
        return params;
    }
}
