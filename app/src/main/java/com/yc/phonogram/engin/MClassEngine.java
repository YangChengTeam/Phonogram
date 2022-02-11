package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.MClassListInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by zhangkai on 2017/12/19.
 */

public class MClassEngine extends BaseEngine {

    public MClassEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<MClassListInfo>> getMClassList() {


        return request.getMClassList(appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
