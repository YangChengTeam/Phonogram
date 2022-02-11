package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.helper.UserInfoHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by wanglin  on 2018/5/15 16:28.
 */

public class PaySuccessEngine extends BaseEngine {
    public PaySuccessEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<String>> uploadPhone(String phone) {

        return request.uploadPhone(UserInfoHelper.getUid(), phone, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
