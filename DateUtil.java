package com.realmo.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: DateUtil
 * @Author: Administrator
 * @Date: 2016/5/26 17:03
 */
public class DateUtil {

    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static String stringToString(String dateString) {
//        ParsePosition position = new ParsePosition(0);
        String str = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date dateValue = null;
        try {
            dateValue = simpleDateFormat.parse(dateString);
            str = simpleDateFormat.format(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
