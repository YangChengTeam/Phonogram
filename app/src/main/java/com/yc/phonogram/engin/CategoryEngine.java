package com.yc.phonogram.engin;


import android.content.Context;

import com.yc.phonogram.domain.CategoryInfoWrapperWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by wanglin  on 2019/5/15 10:02.
 */
public class CategoryEngine extends BaseEngine {
    public CategoryEngine(Context context) {
        super(context);
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

        return request.getCategorys(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
