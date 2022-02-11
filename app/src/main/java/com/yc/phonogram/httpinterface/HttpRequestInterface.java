package com.yc.phonogram.httpinterface;


import android.content.Context;

import com.yc.phonogram.domain.AdvInfoWrapper;
import com.yc.phonogram.domain.CategoryDetailInfo;
import com.yc.phonogram.domain.CategoryInfoWrapperWrapper;
import com.yc.phonogram.domain.GoodListInfo;
import com.yc.phonogram.domain.LoginDataInfo;
import com.yc.phonogram.domain.MClassListInfo;
import com.yc.phonogram.domain.MyOrderInfoWrapper;
import com.yc.phonogram.domain.PhonogramListInfo;
import com.yc.phonogram.domain.UserInfo;
import com.yc.phonogram.domain.UserInfoWrapper;
import com.yc.phonogram.pay.OrderInfo;
import com.yc.phonogram.pay.OrderParamsInfo;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import yc.com.rthttplibrary.bean.ResultInfo;


/**
 * Created by suns  on 2020/7/29 11:31.
 */
public interface HttpRequestInterface {

    @POST("index/init")
    Observable<ResultInfo<LoginDataInfo>> getIndexInfo(@Query("app_id") String app_id);


    @POST("index/vip_list")
    Observable<ResultInfo<GoodListInfo>> getGoodList(@Query("app_id") String app_id);

    @POST("index/phonetic_list")
    Observable<ResultInfo<PhonogramListInfo>> getPhonogramList(@Query("app_id") String app_id);

    @POST("index/phonetic_class")
    Observable<ResultInfo<MClassListInfo>> getMClassList(@Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("index/user_edit")
    Observable<ResultInfo<String>> uploadPhone(@Field("user_id") String user_id, @Field("mobile") String phone, @Query("app_id") String app_id);

    @POST("index/menu_adv")
    Observable<ResultInfo<AdvInfoWrapper>> getAdvInfo(@Query("app_id") String app_id);

    @POST("weike/index")
    Observable<ResultInfo<CategoryInfoWrapperWrapper>> getCategorys(@Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("weike/detail")
    Observable<ResultInfo<CategoryDetailInfo>> getCategoryDetail(@Field("id") int id, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("user/login")
    Observable<ResultInfo<UserInfoWrapper>> login(@Field("username") String username, @Field("pwd") String password, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("user/send_code")
    Observable<ResultInfo<String>> sendCode(@Field("mobile") String phone, @Query("app_id") String app_id);


    @FormUrlEncoded
    @POST("user/reg")
    Observable<ResultInfo<UserInfoWrapper>> register(@Field("mobile") String mobile, @Field("code") String code, @Field("pwd") String pwd, @Query("app_id") String app_id);


    @FormUrlEncoded
    @POST("user/upd_pwd")
    Observable<ResultInfo<UserInfo>> modifyPwd(@Field("user_id") String user_id, @Field("pwd") String pwd, @Field("new_pwd") String newPwd, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("user/code_login")
    Observable<ResultInfo<UserInfoWrapper>> codeLogin(@Field("mobile") String mobile, @Field("code") String code, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("user/set_pwd")
    Observable<ResultInfo<String>> setPwd(@Field("user_id") String user_id, @Field("new_pwd") String pwd, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("orders/lists")
    Observable<ResultInfo<MyOrderInfoWrapper>> getOrderList(@Field("user_id") String user_id, @Field("page") int page, @Field("page_size") int page_size, @Query("app_id") String app_id);

    @FormUrlEncoded
    @POST("index/pay")
    Observable<ResultInfo<OrderInfo>> pay(@FieldMap Map<String, String> params, @Query("app_id") String app_id);


    @FormUrlEncoded
    @POST
    Observable<ResultInfo<String>> checkOrder(@Url String url, @FieldMap Map<String, String> params);

}
