package com.yc.phonogram.helper;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.domain.VipInfo;
import com.yc.phonogram.ui.popupwindow.LoginPopupWindow;

import java.util.List;

import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by suns  on 2020/4/7 10:01.
 */
public class UserInfoHelper {
    private static UserInfo mUserInfo;

    private static List<VipInfo> vipInfos;

    public static void saveUser(UserInfo userInfo) {
        mUserInfo = userInfo;
        try {
            String str = JSON.toJSONString(userInfo);
            SharePreferenceUtils.getInstance().putString(Config.USER_INFO, str);

        } catch (Exception e) {
            LogUtil.msg("json数据保存失败： " + e.getMessage());
        }
    }

    public static UserInfo getUser() {
        if (null != mUserInfo) {
            return mUserInfo;
        }
        try {
            String str = SharePreferenceUtils.getInstance().getString(Config.USER_INFO, "");
            mUserInfo = JSON.parseObject(str, UserInfo.class);
            return mUserInfo;

        } catch (Exception e) {
            LogUtil.msg("json数据解析失败：" + e.getMessage());
        }
        return null;
    }


    public static void saveVipList(List<VipInfo> vipInfoList) {
        vipInfos = vipInfoList;
        try {
            String str = JSON.toJSONString(vipInfoList);
            SharePreferenceUtils.getInstance().putString(Config.VIP_INFOS, str);
        } catch (Exception e) {
            LogUtil.msg("vip数据保存失败： " + e.getMessage());
        }
    }

    public static List<VipInfo> getVipInfos() {
        if (vipInfos != null) {
            return vipInfos;
        }
        try {
            String str = SharePreferenceUtils.getInstance().getString(Config.VIP_INFOS, "");
            vipInfos = JSON.parseArray(str, VipInfo.class);
            return vipInfos;
        } catch (Exception e) {
            LogUtil.msg("vip数据解析失败：" + e.getMessage());
        }
        return null;
    }

    public static String getUid() {
        String userId = "";
        if (null != getUser()) {
            userId = getUser().getUserId();
        }
        return userId;
    }

    public static boolean isLogin(Activity activity) {
        boolean isLogin = false;
        if (!TextUtils.isEmpty(getUid())) {
            isLogin = true;
        }
        if (!isLogin) {
//            String phone = SharePreferenceUtils.getInstance().getString(Config.USER_PHONE, "");
//            if (TextUtils.isEmpty(phone)) {
//                RegisterPopupWindow registerPopupWindow = new RegisterPopupWindow(activity);
//                registerPopupWindow.show();
//            } else {
            LoginPopupWindow loginPopupWindow = new LoginPopupWindow(activity);
            loginPopupWindow.show();
//            }
        }
        return isLogin;
    }


    public static void logout() {

//        if (null != userInfo) {
//            SharePreferenceUtils.getInstance().putString(Config.USER_PHONE, userInfo.getMobile());
//        }
        UserInfo userInfo = new UserInfo();
        UserInfoHelper.saveUser(userInfo);
        List<VipInfo> vipInfos = getVipInfos();
        if (vipInfos != null && vipInfos.size() > 0) {
            vipInfos = null;
        }
        saveVipList(vipInfos);
    }

}
