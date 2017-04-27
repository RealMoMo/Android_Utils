package com.realmo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by realmo on 16/8/15.
 */
public class LocalUtils {
    /**
     * 判断国家是否是国内用户
     *
     *方法一
     *
     * @return
     */
    public static boolean isCN(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryIso = tm.getSimCountryIso();
        boolean isCN = false;//判断是不是大陆
        if (!TextUtils.isEmpty(countryIso)) {
            countryIso = countryIso.toUpperCase(Locale.US);
            if (countryIso.contains("CN")) {
                isCN = true;
            }
        }
        return isCN;

    }

    /**
     * 判断系统语言环境
     * @return
     */

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }



/**
 * 方法二
 */
    /** 查询手机的 MCC+MNC */
    private static String getSimOperator(Context c) {
        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSimOperator();
        } catch (Exception e) {

        }
        return null;
    }


    /** 因为发现像华为Y300，联想双卡的手机，会返回 "null" "null,null" 的字符串 */
    private static boolean isOperatorEmpty(String operator) {
        if (operator == null) {
            return true;
        }


        if (operator.equals("") || operator.toLowerCase(Locale.US).contains("null")) {
            return true;
        }


        return false;
    }


    /** 判断是否是国内的 SIM 卡，优先判断注册时的mcc */
    public static boolean isChinaSimCard(Context c) {
        String mcc = getSimOperator(c);
        if (isOperatorEmpty(mcc)) {
            return false;
        } else {
            return mcc.startsWith("460");
        }
    }



}
