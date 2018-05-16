package com.realmo.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;


/**
 * 获取手机相关信息
 */
public class PhoneUtil {
/**
     * 获取当前应用程序的包名
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }
    /**
     * 获取手机屏幕宽度
     */
    public static int getPhoneWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机屏幕高度
     */
    public static int getPhoneHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取控件高度
     */
    public static int getWidgetHeight(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredHeight();
    }

    /**
     * 获取控件宽度
     */
    public static int getWidgetWidth(View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return v.getMeasuredWidth();
    }

    /**
     * dp 转 px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取手机ip
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface ni = (NetworkInterface) en.nextElement();
                for (Enumeration netAddress = ni.getInetAddresses(); netAddress.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) netAddress.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App名称
     */
    public static String getAppName(Context context) {
        String appName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
            appName = (String) packageManager.getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 获取App版本号
     */
    public static String getAppVersion(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 是否需要更新
     * return  0 -- 不需要更新、1 -- 需要更新、2 -- 需要强制更新
     */
    public static int isNeedUpdate(String thisVersion, String newVersion) {
        try {
            if (thisVersion.equals(newVersion)) {
                return 0;
            }
            int times = 100;//倍率
            float currTimes;//当前倍数
            float v1 = 0;//当前版本计算值
            float v2 = 0;//服务器版本计算值
            currTimes = 0.01F;
            String[] ver1 = thisVersion.split("\\.");
            for (int i = ver1.length - 1; i >= 0; i--) {
                v1 += Integer.parseInt(ver1[i]) * currTimes;
                currTimes *= times;
            }
            currTimes = 0.01F;
            String[] ver2 = newVersion.split("\\.");
            if (Integer.parseInt(ver1[0]) < Integer.parseInt(ver2[0])) {//有重大更新
                return 2;
            }
            for (int i = ver1.length - 1; i >= 0; i--) {
                v2 += Integer.parseInt(ver2[i]) * currTimes;
                currTimes *= times;
            }
            if (v2 > v1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            if (!thisVersion.equals(newVersion)) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    /**
     * 手机号加*
     */
    public static String getMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "****";
        } else {
            if (mobile.length() >= 7) {
                return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
            } else {
                return mobile + "****";
            }
        }
    }

    /**
     * 本地缓存是否可用
     */
    public static boolean isUrlCacheValid(Context context, String url) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (TextUtils.isEmpty(CacheUtil.getUrlCache(url))) {
            return false;
        } else {
            try {
                SharedPreferences isFirst = context.getSharedPreferences("isFirst", context.MODE_PRIVATE);
                String lastModifyTime = isFirst.getString("LastAddressModifyTime", sdf.format(new Date()));
                long cacheModifyTime = CacheUtil.getUrlCacheModifyTime(url);
                if (cacheModifyTime > sdf.parse(lastModifyTime).getTime()) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    /**
     * 弹出键盘
     */
    public static void showKeyboard(final View v) {
        v.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
    }

    /**
     * 隐藏键盘
     */
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 复制到剪贴板
     */
    public static void copyBoard(Context context, String str) {
        ClipboardManager boardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        boardManager.setPrimaryClip(ClipData.newPlainText(null, str));
    }

    /**
     * 判断EditText输入是否错误 -- 判断2个字符
     */
    public static void isEditError2(String text, EditText editText) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("\"")) {
                editText.setError("不可包含 \" 号");
            }
            if (text.contains("\\")) {
                editText.setError("不可包含 \\ 号");
            }
        } else {
            editText.setError(null);
        }
    }

    /**
     * 判断EditText输入是否错误 -- 判断2个字符 带删除按钮
     */
    public static void isEditError2WithDel(String text, EditText editText, View delBtn) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("\"")) {
                editText.setError("不可包含 \" 号");
            }
            if (text.contains("\\")) {
                editText.setError("不可包含 \\ 号");
            }
            delBtn.setVisibility(View.VISIBLE);
        } else {
            editText.setError(null);
            delBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 判断EditText输入是否错误 -- 判断2个字符 带删除按钮、带可输入字数
     */
    public static void isEditError2WithDelWithNum(String text, EditText editText, View delBtn, TextView tvLimit) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains("\"")) {
                editText.setError("不可包含 \" 号");
            }
            if (text.contains("\\")) {
                editText.setError("不可包含 \\ 号");
            }
            delBtn.setVisibility(View.VISIBLE);
            tvLimit.setText((140 - text.length()) + "");
        } else {
            editText.setError(null);
            delBtn.setVisibility(View.GONE);
            tvLimit.setText(140 + "");
        }
    }

    /**
     * 判断EditText输入是否错误 -- 判断3个字符
     */
    public static void isEditError3(String text, EditText editText) {
        if (!TextUtils.isEmpty(text)) {
            isEditError2(text, editText);
            if (text.contains("|")) {
                editText.setError("不可包含 | 号");
            }
        } else {
            editText.setError(null);
        }
    }

    /**
     * 判断EditText输入是否错误 -- 判断4个字符
     */
    public static void isEditError4(String text, EditText editText) {
        if (!TextUtils.isEmpty(text)) {
            isEditError3(text, editText);
            if (text.contains("_")) {
                editText.setError("不可包含 _ 号");
            }
        } else {
            editText.setError(null);
        }
    }

    /**
     * 数字转换为二位小数
     */
    public static String formatDecimal(double d, int pointNum) {
        StringBuffer format = new StringBuffer("0");
        for (int i = 0; i < pointNum; i++) {
            if (i == 0) {
                format.append(".");
            }
            format.append("0");
        }
        return new DecimalFormat(format.toString()).format(d);
    }

    /**
     * 格式化当前时间
     */
    public static String formatDate() {
        SimpleDateFormat sdf24H = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.getDefault());
        return sdf24H.format(new Date());
    }

    /**
     * 获取尾数
     */
    public static String getEndNum(String s, int endNum) {
        if (!TextUtils.isEmpty(s) && s.length() > endNum) {
            return s.substring(s.length() - endNum);
        }
        return s;
    }

    /**
     * 处理金额  超过1万用简写
     */
    public static String handleBalance(String balance, int pointNum) {
        double d = Double.parseDouble(balance);
        if (d <= 10000) {
            return formatDecimal(d, pointNum);
        } else {
            return formatDecimal(d / 10000, pointNum) + "万";
        }
    }

    public static String handleBalance2(String balance, int pointNum) {
        double d = Double.parseDouble(balance);
        if (d < 10000) {
            return formatDecimal(d, pointNum);
        } else {
            return formatDecimal(d / 10000, pointNum) + "万";
        }
    }

    /**
     * 处理日期
     */
    public static String handleTime(String time) {
        if (!TextUtils.isEmpty(time) && time.endsWith(".000")) {
            return time.substring(0, time.lastIndexOf("."));
        }
        return time;
    }

    /**
     * 拼接收货地址
     */
    /*public static String stitchAddress(AddressEntity.DataEntity d){
        String address = d.getProvince() + "_" + d.getCity() + "_" + d.getCounty() + "_" + d.getAddress();
        return d.getRealName() + "|" + d.getMobile() + "|" + d.getZipCode() + "|" + address;
    }*/
   /* public static String getLocalMobile(Context context) {
        UserInfoEntity.DataEntity userInfo = getUserInfo(context);
        if (userInfo != null) {
            return userInfo.getMobile();
        }
        return null;
    }*/

  /*  *//**
     * 获取本地的用户信息
     *//*
    public static UserInfoEntity.DataEntity getUserInfo(Context context) {
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        String info = userInfo.getString("UserInfo", null);
        if (info != null) {
            try {
                info = DesUtil.decrypt(info, DesUtil.LOCAL_KEY);
                UserInfoEntity infoEntity = new Gson().fromJson(info, UserInfoEntity.class);
                return infoEntity.getData();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/


    /**
     * 转换为字符串日期
     */
    public static String getDateOfNum(int year, int month, int day) {
        DecimalFormat format2 = new DecimalFormat("00");
        return year + "-" + format2.format(month) + "-" + format2.format(day);
    }

}
