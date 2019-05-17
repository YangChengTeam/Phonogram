package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.domain.CategoryDetailInfo;
import com.yc.phonogram.domain.Config;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2019/5/16 12:28.
 */
public class CategoryDetailEngine extends BaseEngin {
    public CategoryDetailEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.CATEGORY_DETAIL;
    }

    public Observable<ResultInfo<CategoryDetailInfo>> getCategoryDetail(int id) {

        Map<String, String> params = new HashMap<>();

        params.put("id", id + "");

        return rxpost(new TypeReference<ResultInfo<CategoryDetailInfo>>() {
        }.getType(), params, true, true, true);


    }

}
