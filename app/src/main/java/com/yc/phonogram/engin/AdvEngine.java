package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.domain.AdvInfoWrapper;
import com.yc.phonogram.domain.Config;

import rx.Observable;

/**
 * Created by wanglin  on 2019/1/25 09:55.
 */
public class AdvEngine extends BaseEngin<ResultInfo<AdvInfoWrapper>> {
    public AdvEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.ADV_INFO;
    }

    public Observable<ResultInfo<AdvInfoWrapper>> getAdvInfo() {
        return rxpost(new TypeReference<ResultInfo<AdvInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }
}
