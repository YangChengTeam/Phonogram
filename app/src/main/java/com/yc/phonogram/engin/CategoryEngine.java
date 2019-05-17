package com.yc.phonogram.engin;


import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.switfpass.pay.bean.MchBean;
import com.yc.phonogram.R;
import com.yc.phonogram.domain.CategoryInfo;
import com.yc.phonogram.domain.CategoryInfoWrapperWrapper;
import com.yc.phonogram.domain.Config;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2019/5/15 10:02.
 */
public class CategoryEngine extends BaseEngin {
    public CategoryEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.CATEGORY_LIST_INDEX;
    }

//    public Observable<List<CategoryInfo>> getCategoryInfos() {
//
//
//        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map(new Func1<String, List<CategoryInfo>>() {
//            @Override
//            public List<CategoryInfo> call(String s) {
//                List<CategoryInfo> categoryInfos = new ArrayList<>();
//
//                categoryInfos.add(new CategoryInfo(1, R.mipmap.category1, 1));
//                categoryInfos.add(new CategoryInfo(2, R.mipmap.category2, 1));
//                categoryInfos.add(new CategoryInfo(3, R.mipmap.category3, 2));
//                categoryInfos.add(new CategoryInfo(4, R.mipmap.category4, 2));
//                categoryInfos.add(new CategoryInfo(5, R.mipmap.category5, 2));
//                categoryInfos.add(new CategoryInfo(6, R.mipmap.category6, 2));
//                categoryInfos.add(new CategoryInfo(7, R.mipmap.category7, 2));
//                categoryInfos.add(new CategoryInfo(8, R.mipmap.category8, 2));
//                categoryInfos.add(new CategoryInfo(9, R.mipmap.category9, 2));
//                categoryInfos.add(new CategoryInfo(10, R.mipmap.category10, 2));
//                categoryInfos.add(new CategoryInfo(11, R.mipmap.category11, 2));
//                categoryInfos.add(new CategoryInfo(12, R.mipmap.category12, 2));
//
//                return categoryInfos;
//            }
//        });
//    }

    public Observable<ResultInfo<CategoryInfoWrapperWrapper>> getCategorys() {
        return rxpost(new TypeReference<ResultInfo<CategoryInfoWrapperWrapper>>() {
        }.getType(), null, true, true, true);
    }
}
