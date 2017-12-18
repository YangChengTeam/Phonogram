package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.GoodListInfo;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class Good2Engin extends BaseEngin<ResultInfo<GoodListInfo>> {
    public Good2Engin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.VIP_LIST2_URL;
    }

    public Observable<ResultInfo<GoodListInfo>> getGoodList(String tid) {
        Map<String, String> params = new HashMap<>();
        params.put("flag", tid);
        return rxpost(new TypeReference<ResultInfo<GoodListInfo>>() {
        }.getType(), params, true, true, true);
    }

}
