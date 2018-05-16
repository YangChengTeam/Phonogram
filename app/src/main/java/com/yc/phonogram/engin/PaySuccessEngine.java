package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.App;
import com.yc.phonogram.domain.Config;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2018/5/15 16:28.
 */

public class PaySuccessEngine extends BaseEngin<ResultInfo<String>> {
    public PaySuccessEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.UPLOAD_PHONE_URL;
    }

    public Observable<ResultInfo<String>> uploadPhone(String phone) {
        Map<String, String> params = new HashMap<>();
        String user_id = "";
        if (App.getApp().getLoginDataInfo() != null && App.getApp().getLoginDataInfo().getStatusInfo() != null) {
            user_id = App.getApp().getLoginDataInfo().getStatusInfo().getUid();
        }
        params.put("user_id", user_id);
        params.put("mobile", phone);
        return rxpost(new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }
}
