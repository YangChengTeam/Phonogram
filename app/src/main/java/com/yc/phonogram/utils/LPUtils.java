package com.yc.phonogram.utils;


import android.text.TextUtils;
import android.util.Log;
import java.util.regex.Pattern;

/**
 * TinyHung@Outlook.com
 * 2017/12/20.
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
            return word.replace(letter,"<font color='#FD0000'>"+letter);
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

        if(null==phonetic) return phonetic;
        Pattern pattern = Pattern.compile("/");
        String[] strs = pattern.split(phonetic);
        if(null!=strs&&strs.length>0){
            StringBuffer stringBuffer=new StringBuffer();
            for (String str : strs) {
                Log.d(TAG,"str="+str);
                if(!TextUtils.equals("/",str)){
                    stringBuffer.append(str);
                }
            }
            Log.d(TAG,"stringBuffer="+stringBuffer);
            if(phonetic.contains(stringBuffer.toString())){
                return phonetic.replace(stringBuffer.toString(),"<font color='#FD0000'>"+stringBuffer.toString());
            }
        }
        return phonetic;
    }
}
