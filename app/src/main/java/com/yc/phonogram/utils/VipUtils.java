package com.yc.phonogram.utils;

import android.content.Context;

import com.kk.utils.PreferenceUtil;
import com.yc.phonogram.App;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.domain.VipInfo;

import java.util.List;

public class VipUtils {

    private static VipUtils instance;
    private final Context mContext;

    private VipUtils(Context context){
        this.mContext =context;
    }

    public static VipUtils getInstance(Context context){
        synchronized (VipUtils.class){
            if (instance==null){
                synchronized (VipUtils.class){
                    instance = new VipUtils(context);
                }
            }
        }
        return instance;
    }

    private boolean isVip(String vip) {
        boolean flag = false;
        String vips = PreferenceUtil.getImpl(mContext).getString("vip", "");
        String[] vipArr = vips.split(",");

        for (String tmp : vipArr) {
            if (tmp.equals(vip)) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            LoginDataInfo loginDataInfo = App.getApp().getLoginDataInfo();
            if (loginDataInfo != null) {
                List<VipInfo> vipInfoList = loginDataInfo.getVipInfoList();
                if (vipInfoList != null) {
                    for (VipInfo vipInfo : vipInfoList) {
                        if (vip.equals(vipInfo.getType() + "")) {
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        return flag;
    }

    public boolean isVip() {
        return isVip(Config.PHONOGRAM_VIP + "") || isVip(Config.PHONICS_VIP + "") || isVip(Config.PHONOGRAMORPHONICS_VIP + "")
                || isVip(Config.SUPER_VIP + "") || isVip(Config.CORRECTPRONUNCIATION_VIP + "") || isVip(Config.CORRECTPRONUNCIATIONPROMISS_VIP + "");
    }
}
