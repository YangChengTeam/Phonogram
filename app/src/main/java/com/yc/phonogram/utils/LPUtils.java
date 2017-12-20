package com.yc.phonogram.utils;

import android.text.TextUtils;

/**
 * TinyHung@Outlook.com
 * 2017/12/20.W
 */

public class LPUtils {

    private static final String TAG = LPUtils.class.getSimpleName();

    /**
     * 给单词的双舌音部分加上颜色
     * @param word 源单词
     * @param letter 双舌音部分
     * @return
     */
    public static String addWordLetterColor(String word, String letter) {
        if(TextUtils.isEmpty(word))return word;
        if(TextUtils.isEmpty(letter))return word;
        if(word.contains(letter)){
            return word.replace(letter,"<font color='#FD0000'>"+letter+"</font>");
        }
        return word;
    }

    /**
     * 给音标加上颜色
     * @param phonetic 源音标
     * @param letter
     * @return
     */
    public static String addWordPhoneticLetterColor(String phonetic, String letter) {
        if(TextUtils.isEmpty(phonetic))return phonetic;
        if(TextUtils.isEmpty(letter))return phonetic;
        String replace = letter.replace("/", "");
            if(phonetic.contains(replace)){
                return phonetic.replace(replace,"<font color='#FD0000'>"+replace+"</font>");
            }
        return phonetic;
    }
}
