package com.yc.phonogram.domain;

/**
 * Created by zhangkai on 2017/10/19.
 */

public class Config {
    public static boolean DEBUG = false;

    public final static int PHONOGRAM_VIP = 1;//点读
    public final static int PHONICS_VIP = 2;//微课
    public final static int PHONOGRAMORPHONICS_VIP = 3;//点读+微课
    public final static int SUPER_VIP = 4;//点读+微课+小班辅导纠音
    public final static int CORRECTPRONUNCIATION_VIP = 12;//点读+微课+1对1纠音
    public final static int CORRECTPRONUNCIATIONPROMISS_VIP = 13;//点读+微课+1对1纠音+学习保

    public static final String APPID = "?app_id=5";

    public static final String INIT_URL = getBaseUrl() + "index/init" + APPID;
    public static final String ORDER_URL = getBaseUrl() + "index/pay" + APPID;
    public static final String VIP_LIST_URL = getBaseUrl() + "index/vip_list" + APPID;
    public final static String CHECK_URL = getBaseUrl() + "index/orders_check" + APPID;
    public final static String QUERY_URL = getBaseUrl() + "index/orders_query" + APPID;
    public static final String PAY_WAY_LIST_URL = getBaseUrl() + "index/payway_list" + APPID;
    public static final String TYPE_LIST_URL = getBaseUrl() + "index/vip_flag_list" + APPID;

    public static final String PHONOGRAM_LIST_URL = getBaseUrl() + "index/phonetic_list" + APPID;
    public static final String MCLASS_LIST_URL = getBaseUrl() + "index/phonetic_class" + APPID;

    public static final String UPLOAD_PHONE_URL = getBaseUrl() + "index/user_edit" + APPID;
    public static final String AV_APPID = "1108023815";
    public static final String AV_SPLASH_ID = "3020958021271852";

    public static final String ADV_INFO = getBaseUrl() + "index/menu_adv" + APPID;

    public static final String CATEGORY_LIST_INDEX = getBaseUrl() + "weike/index" + APPID;

    public static final String CATEGORY_DETAIL = getBaseUrl() + "weike/detail" + APPID;


    public static final String CATEGORY_INFO = "category_info";

    public static String getBaseUrl() {
        String baseUrl = "http://tic.upkao.com/api/";
        String debugBaseUrl = "http://120.76.202.236:1980/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
