package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.LoginDataInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by zhangkai on 2017/2/20.
 */

public class LoginEngine extends BaseEngine {
    public LoginEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<LoginDataInfo>> rxGetInfo() {
        return request.getIndexInfo(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
