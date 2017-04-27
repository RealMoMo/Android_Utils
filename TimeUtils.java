package com.realmo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/12.
 */

public class TimeUtils {

    public static String FROMAT_CN_NO_TIME = "yyyy-MM-dd";


    public static String dateToString(Date date, String dateFormat) {
        String str;
        //String dateFormat = context.getString(R.string.format_date_full);
        DateFormat format = new SimpleDateFormat(dateFormat);
        str = format.format(date);
//        if (type.equals("SHORT")) {
//            // 07-1-18
//            format = DateFormat.getDateInstance(DateFormat.SHORT);
//            str = format.format(date);
//        } else if (type.equals("MEDIUM")) {
//            // 2007-1-18
//            format = DateFormat.getDateInstance(DateFormat.MEDIUM);
//            str = format.format(date);
//        } else if (type.equals("LONG")) {
//            // 2007-1-18
//            format = DateFormat.getDateInstance(DateFormat.LONG);
//            str = format.format(date);
//        }else if (type.equals("FULL")) {
//            // 2007年1月18日 星期四
//            format = DateFormat.getDateInstance(DateFormat.FULL);
//            str = format.format(date);
//        }
        return str;
    }

    public static Date stringToDate(String str, String dateFormat) {

        DateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
        //date = java.sql.Date.valueOf(str);

        return date;
    }


    public static String standard(String strDate, String from, String to) {
        String toDate = null;

        Date date = stringToDate(strDate, from);
        toDate = dateToString(date, to);

        return toDate;
    }


//    public static void main(String[] args) {
//        Date date = new Date();
//        System.out.println(StringOrDate.dateToString(date, "MEDIUM"));
//        String str = "2012-2-24";
//        System.out.println(StringOrDate.stringToDate(str));
//    }

}
