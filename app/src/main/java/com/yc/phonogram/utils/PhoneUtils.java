package com.yc.phonogram.utils;

import java.util.regex.Pattern;

/**
 * Created by wanglin  on 2018/5/15 16:34.
 */

public class PhoneUtils {

    public static boolean isPhone(CharSequence charSequence) {
        String regex = "^1[2|3|4|5|6|7|8|9]\\d{9}$";
        return Pattern.matches(regex, charSequence);
    }
}
