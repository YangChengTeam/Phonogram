package com.yc.phonogram.domain;

/**
 * Created by zhangkai on 2017/10/19.
 */

public class GoodInfo {
    public String getVipid() {
        return vipid;
    }

    public void setVipid(String vipid) {
        this.vipid = vipid;
    }

    private String vipid;
    private int id;


    private String add_id;
    private String title;
    private String price;
    private String real_price;
    private String desp;
    private String pay_name;

    private String icon;
    private String preview;
    private boolean is_vip;
    private boolean is_free;
    private boolean is_download;

    public boolean is_download() {
        return is_download;
    }

    public void setIs_download(boolean is_download) {
        this.is_download = is_download;
    }

    public boolean is_free() {
        if (Float.parseFloat(real_price) == 0) {
            is_free = true;
        }
        return is_free;
    }

    public void setIs_free(boolean is_free) {
        this.is_free = is_free;
    }

    public boolean is_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getWxpayway() {
        if (wxpayway == null || wxpayway.isEmpty()) {
            wxpayway = "ipaynow";
        }
        return wxpayway;
    }

    public void setWxpayway(String wxpayway) {
        this.wxpayway = wxpayway;
    }

    public String getAlipayway() {
        if (alipayway == null || alipayway.isEmpty()) {
            alipayway = "alipay";
        }
        return alipayway;
    }

    public void setAlipayway(String alipayway) {
        this.alipayway = alipayway;
    }

    private String wxpayway;
    private String alipayway;

    private String alias;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdd_id() {
        return add_id;
    }

    public void setAdd_id(String add_id) {
        this.add_id = add_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReal_price() {
        return real_price;
    }

    public void setReal_price(String real_price) {
        this.real_price = real_price;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
