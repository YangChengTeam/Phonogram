package com.yc.phonogram.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.phonogram.domain.Config;
import com.yc.phonogram.pay.OrderInfo;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/3/14.
 */

public class CheckEngin extends BaseEngin<ResultInfo<String>> {
    public CheckEngin(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return Config.CHECK_URL;
    }


    public Observable<ResultInfo<String>> checkOrder(OrderInfo orderInfo) {
        Map<String, String> parms = new HashMap<>();
        parms.put("goods_id", orderInfo.getViptype() + "");
        parms.put("goods_num", 1 + "");
        parms.put("type", 0 + "");
        parms.put("money", orderInfo.getMoney() + "");
        parms.put("add_time", orderInfo.getAddtime() + "");
        parms.put("order_sn", orderInfo.getOrder_sn());
        parms.put("payway_name", orderInfo.getPayway());
        parms.put("is_payway_split", 1 + "");
        return rxpost(new TypeReference<ResultInfo<String>>() {
        }.getType(), parms, true, true, true);
    }


}
