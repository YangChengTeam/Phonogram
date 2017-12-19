package com.yc.phonogram.domain;

/**
 * Created by wanglin  on 2017/12/18 17:45.
 */

public class GoodItemInfo {
    private String title;
    private String subTitle;
    private String originPrice;
    private String currentPrice;
    private int iconId;

    public GoodItemInfo(String title, String subTitle, String originPrice, String currentPrice, int iconId) {
        this.title = title;
        this.subTitle = subTitle;
        this.originPrice = originPrice;
        this.currentPrice = currentPrice;
        this.iconId = iconId;
    }

    public GoodItemInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(String originPrice) {
        this.originPrice = originPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
