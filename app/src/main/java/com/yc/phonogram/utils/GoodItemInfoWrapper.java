package com.yc.phonogram.utils;

import android.content.Context;

import com.yc.phonogram.R;
import com.yc.phonogram.domain.GoodItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/12/18 17:52.
 */

public class GoodItemInfoWrapper {
    static String[] titles;
    static String[] subTitles;
    static int[] icons;
    static String[] originPrices;
    static String[] currentPrices;

    static {
        titles = new String[]{"音标课堂", "自然拼读法", "音标课堂+自然拼读法", "音标课堂+自然拼读法+学习宝"};
        subTitles = new String[]{"每天十分钟，十天就会读单词", "单词能力提高必选的12节微课", "", "坚持打卡分享学习30天，学不会退款"};
        icons = new int[]{R.mipmap.good_info_num1, R.mipmap.good_info_num2, R.mipmap.good_info_num3, R.mipmap.good_info_num4};
        originPrices = new String[]{"59", "60", "119", "199"};
        currentPrices = new String[]{"19.00", "29.00", "38.00", "99.00"};
    }


    public static List<GoodItemInfo> getGoodItemInfos(Context context) {
        List<GoodItemInfo> goodItemInfos = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            GoodItemInfo goodItemInfo = new GoodItemInfo(titles[i], subTitles[i],
                    String.format(context.getString(R.string.origin_price), originPrices[i]),
                    String.format(context.getString(R.string.current_price), currentPrices[i]), icons[i]);
            goodItemInfos.add(goodItemInfo);
        }

        return goodItemInfos;

    }
}
