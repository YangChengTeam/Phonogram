package com.yc.phonogram.utils;

import android.content.Context;


import com.yc.phonogram.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import yc.com.rthttplibrary.util.LogUtil;


/**
 * Created by wanglin  on 2018/12/21 15:26.
 */
public class AssetsUtil {

    public static Properties getProperties(Context context, String fileName) {

        Properties properties = new Properties();

        try {

            InputStream is = context.getAssets().open(fileName);

            properties.load(is);

        } catch (Exception e) {
            LogUtil.msg("read properties error " + e.getMessage());
        }

        return properties;

    }


    public static Properties getProperties(Context context) {

        return getProperties(context, "devices.properties");

    }


    public static String readAsset(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuilder result = new StringBuilder();


            while ((line = br.readLine()) != null)
                result.append(line.replace("XX", context.getString(R.string.app_name))).append("\n");
            return result.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
