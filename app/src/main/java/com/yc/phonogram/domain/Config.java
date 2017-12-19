package com.yc.phonogram.domain;

import android.os.Environment;

/**
 * Created by zhangkai on 2017/10/19.
 */

public class Config {
    public static boolean DEBUG = false;

    public final static String INDEX_URL = "http://u.wk990.com/dk/share.html?jnk";

    public static final String PATH = Environment.getExternalStorageDirectory() + "/";

    public final static String WEIXIN_JUMP_URL = "http://c.xq12.com/ad/g?p=11";
    public final static String WEIXIN = "jinengkuang";
    public static String QQ = "2683232504";
    public static String VIP_QQ = "2683232504";
    public final static int VIP_ID = 51;


    public final static String DEFAULT_ICON = "http://db.wk990.com/img/wzmhk/icon/26.png";
    public final static String GAME_PACKAGE_NAME = "com.tencent.tmgp.sgame";

    public final static int ALIPAY = 0;
    public final static int WXPAY = 1;

    public final static int VIP = 0;
    public final static int GOODS = 2;
    public final static int REWARD = 1;

    public static final String APPID = "?app_id=4";

    public static final String INIT_URL = getBaseUrl() + "index/init" + APPID;
    public static final String ORDER_URL = getBaseUrl() + "index/pay" + APPID;
    public static final String VIP_LIST_URL = getBaseUrl() + "index/vip_list" + APPID;
    public final static String CHECK_URL = getBaseUrl() + "index/orders_check" + APPID;
    public final static String QUERY_URL = getBaseUrl() + "index/orders_query" + APPID;
    public static final String PAY_WAY_LIST_URL = getBaseUrl() + "index/payway_list" + APPID;
    public static final String TYPE_LIST_URL = getBaseUrl() + "index/vip_flag_list" + APPID;
    public static final String VIP_LIST2_URL = getBaseUrl() + "index/vip_list2" + APPID;
    public static final String GET_USER_INFO_URL = getBaseUrl() + "index/get_user_info" + APPID;
    public static final String UPADTE_USER_INFO_URL = getBaseUrl() + "index/upd_user_info" + APPID;


    public static String getBaseUrl() {
        String baseUrl = "http://u.wk990.com/api/";
        String debugBaseUrl = "http://120.76.202.236:1980/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
