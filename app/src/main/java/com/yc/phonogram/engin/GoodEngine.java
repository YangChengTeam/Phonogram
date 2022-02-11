package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.GoodListInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by zhangkai on 2017/3/1.
 */

public class GoodEngine extends BaseEngine {
    public GoodEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<GoodListInfo>> getGoodList() {

        return request.getGoodList(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
