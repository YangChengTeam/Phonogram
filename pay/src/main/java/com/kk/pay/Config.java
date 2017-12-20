package com.kk.pay;

/**
 * Created by zhangkai on 2017/10/19.
 */

public class Config {
    public static boolean DEBUG = false;


    public final static int VIP = 0;
    public final static int GOODS = 2;
    public final static int REWARD = 1;


    public static final String APPID = "?app_id=5";

    public final static String CHECK_URL = getBaseUrl() + "index/orders_check" + APPID;

    public static String getBaseUrl() {
        String baseUrl = "http://tic.upkao.com/api/";
        String debugBaseUrl = "http://120.76.202.236:1980/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
