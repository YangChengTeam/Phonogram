package com.kk.pay;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Xml;

import com.kk.utils.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangkai on 2017/4/17.
 */

public class PayImplFactory {
    public static IPayImpl createPayImpl(Activity context, String payway) {

        if (payway == null || payway.isEmpty()) {
            return null;
        }

        List<PayInfo> payInfos = getPayInfos(context);
        PayInfo payInfo = getPayInfo(payInfos, payway);



        IPayImpl iPayImpl = null;

        try {
            Class clazz = Class.forName(payInfo.getClassName());

            if (payInfo != null && payInfo.getType() != null) {
                Class<?>[] parTypes = new Class<?>[2];
                parTypes[0] = Activity.class;
                parTypes[1] = String.class;
                Constructor<?> con = clazz.getConstructor(parTypes);
                iPayImpl = (IPayImpl) con.newInstance(context, payInfo.getType());
            } else {
                Class<?>[] parTypes = new Class<?>[1];
                parTypes[0] = Activity.class;
                Constructor<?> con = clazz.getConstructor(parTypes);
                iPayImpl = (IPayImpl) con.newInstance(context);
            }
        } catch (Exception e) {
            try {
                Class clazz = Class.forName("com.kk.pay.IDunXingH5PayImpl");
                Class<?>[] parTypes = new Class<?>[1];
                parTypes[0] = Activity.class;
                Constructor<?> con = clazz.getConstructor(parTypes);
                iPayImpl = (IPayImpl) con.newInstance(context);
            }catch (Exception e2){

            }
            e.printStackTrace();
        }
        return iPayImpl;
    }


    public static class PayInfo {
        private String payway;
        private String className;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        private String type;

        public String getPayway() {
            return payway;
        }

        public void setPayway(String payway) {
            this.payway = payway;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

    }

    private static PayInfo getPayInfo(List<PayInfo> payInfos, String payway) {
        PayInfo fpayInfo = null;
        for (PayInfo payInfo : payInfos) {
            if (payInfo.getPayway() != null && payInfo.getPayway().equals(payway)) {
                fpayInfo = payInfo;
                break;
            }
        }
        return fpayInfo;
    }

    private static List<PayInfo> getPayInfos(Context context) {
        List<PayInfo> list = new ArrayList<PayInfo>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(context.getAssets().open("payinfo.xml"), "UTF-8");
            int event = parser.getEventType();
            PayInfo payInfo = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("payinfo".equals(parser.getName())) {
                            payInfo = new PayInfo();
                        }
                        if (payInfo != null) {
                            if ("payway".equals(parser.getName())) {
                                payInfo.setPayway(parser.nextText());
                            } else if ("class".equals(parser.getName())) {
                                payInfo.setClassName(parser.nextText());
                            } else if ("type".equals(parser.getName())) {
                                payInfo.setType(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("payinfo".equals(parser.getName())) {
                            list.add(payInfo);
                            payInfo = null;
                        }
                        break;
                }
                event = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
