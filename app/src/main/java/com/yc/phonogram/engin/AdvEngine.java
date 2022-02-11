package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.AdvInfoWrapper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by wanglin  on 2019/1/25 09:55.
 */
public class AdvEngine extends BaseEngine {
    public AdvEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<AdvInfoWrapper>> getAdvInfo() {

        return request.getAdvInfo(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
