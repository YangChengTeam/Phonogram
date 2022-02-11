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

    public static final String VIP_LIST_URL = getBaseUrl() + "index/vip_list" + APPID;


    public static final String PHONOGRAM_LIST_URL = getBaseUrl() + "index/phonetic_list" + APPID;


    public static final String AV_APPID = "1108023815";
    public static final String AV_SPLASH_ID = "3020958021271852";

    public static final String ADV_INFO = getBaseUrl() + "index/menu_adv" + APPID;


    public static final String CATEGORY_INFO = "category_info";

    public static final String index_dialog = "index_dialog";

    public static final String USER_INFO = "user_info";
    public static final String USER_PHONE = "user_phone";

    public static final String VIP_INFOS = "vip_infos";

    public static final String TOUTIAO_AD_ID = "5044636";
    public static final String TOUTIAO_SPLASH_ID = "887291486";

    public static String getBaseUrl() {
        String baseUrl = "http://tic.upkao.com/api/";
//        String baseUrl = "http://tic.bshu.com/api/";
        String debugBaseUrl = "http://120.76.202.236:1980/api/";
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
