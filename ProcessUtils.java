package com.realmo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.List;



public class ProcessUtils {

    /**
     * 获取顶端APK 包名
     * @param context
     * @return
     */
    public static String getCurrentAppPackage(Context context) {
        String result = "";
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT < 21) {
            // 如果没有就用老版本
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
            if (runningTaskInfos != null && runningTaskInfos.size() > 0) {
                result = runningTaskInfos.get(0).topActivity.getPackageName();
            }
        } else {
            List<ActivityManager.RunningAppProcessInfo> runningApp = manager.getRunningAppProcesses();
            if (runningApp != null && runningApp.size() > 0) {
                result = runningApp.get(0).processName;
            }
        }
        if (TextUtils.isEmpty(result)) {
            result = "";
        }
        return result;
    }
}
