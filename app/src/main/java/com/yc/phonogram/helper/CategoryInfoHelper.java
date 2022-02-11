package com.yc.phonogram.helper;

import com.alibaba.fastjson.JSON;
import com.yc.phonogram.domain.CategoryInfoWrapperWrapper;
import com.yc.phonogram.domain.Config;

import yc.com.rthttplibrary.util.LogUtil;

/**
 * Created by wanglin  on 2019/5/17 09:49.
 */
public class CategoryInfoHelper {

    private static CategoryInfoWrapperWrapper infoWrapperWrapper;

    public static CategoryInfoWrapperWrapper getInfoWrapperWrapper() {
        if (infoWrapperWrapper != null) {
            return infoWrapperWrapper;
        }

        try {
            infoWrapperWrapper = JSON.parseObject(SharePreferenceUtils.getInstance().getString(Config.CATEGORY_INFO, ""), CategoryInfoWrapperWrapper.class);

        } catch (Exception e) {
            LogUtil.msg("e:  Json解析错误" + e.getMessage());
        }


        return infoWrapperWrapper;
    }

    public static void setInfoWrapperWrapper(CategoryInfoWrapperWrapper infoWrapperWrapper) {
        CategoryInfoHelper.infoWrapperWrapper = infoWrapperWrapper;
        try {
            String content = JSON.toJSONString(infoWrapperWrapper);
            SharePreferenceUtils.getInstance().putString(Config.CATEGORY_INFO, content);

        } catch (Exception e) {
            LogUtil.msg("e:  对象转Json错误" + e.getMessage());
        }
    }
}
