package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.PhonogramListInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;



public class PhonographEngine extends BaseEngine {
    public PhonographEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<PhonogramListInfo>> getPhonogramList() {


        return request.getPhonogramList(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
