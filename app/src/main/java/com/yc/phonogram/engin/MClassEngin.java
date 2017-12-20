package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.domain.MClassListInfo;

import rx.Observable;

/**
 * Created by zhangkai on 2017/12/19.
 */

public class MClassEngin extends BaseEngin {

    public MClassEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.MCLASS_LIST_URL;
    }

    public Observable<ResultInfo<MClassListInfo>> getMClassList() {
        return rxpost(new TypeReference<ResultInfo<MClassListInfo>>() {

        }.getType(), null, true, true, true);
    }
}
