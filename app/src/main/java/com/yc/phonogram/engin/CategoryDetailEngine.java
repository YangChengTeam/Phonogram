package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.CategoryDetailInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by wanglin  on 2019/5/16 12:28.
 */
public class CategoryDetailEngine extends BaseEngine {
    public CategoryDetailEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<CategoryDetailInfo>> getCategoryDetail(int id) {

        return request.getCategoryDetail(id, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }

}
