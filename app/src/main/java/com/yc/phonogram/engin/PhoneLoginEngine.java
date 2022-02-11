package com.yc.phonogram.engin;

import android.content.Context;

import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.domain.UserInfoWrapper;
import com.yc.phonogram.helper.UserInfoHelper;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by suns  on 2020/4/7 09:01.
 */
public class PhoneLoginEngine extends BaseEngine {
    public PhoneLoginEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<UserInfoWrapper>> login(String username, String password) {



        return request.login(username, password, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> sendCode(String phone) {

        return request.sendCode(phone, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ResultInfo<UserInfoWrapper>> register(String mobile, String code, String pwd) {


        return request.register(mobile, code, pwd, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param pwd    原密码
     * @param newPwd 新密码
     * @return
     */
    public Observable<ResultInfo<UserInfo>> modifyPwd(String pwd, String newPwd) {


        return request.modifyPwd(UserInfoHelper.getUid(), pwd, newPwd, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<UserInfoWrapper>> codeLogin(String mobile, String code) {

        return request.codeLogin(mobile, code, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ResultInfo<String>> setPwd(String pwd) {

        return request.setPwd(UserInfoHelper.getUid(), pwd, appId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }
}
