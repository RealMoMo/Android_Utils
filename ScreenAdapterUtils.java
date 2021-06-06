package com.newline.ucos.launcher.utils;


import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Realmo
 * @version 1.0.0
 * @name 屏幕适配工具类
 * @email momo.weiye@gmail.com
 * @time 2020/4/8 9:20
 * @describe  简易今日头条 屏幕适配方案
 */
public class ScreenAdapterUtils {
    private static float appDensity;
    private static float appScaledDensity;
    private static DisplayMetrics appDisplayMetrics;
    /**
     * 用来参照的的width 对应单位dp
     */
    private static float WIDTH;

    /**
     *
     * @param application
     * @param width  对应屏幕切图宽度的dp值 如：1920px hdpi  则width = 1920F/1.5
     */
    public static void setDensity(@NonNull final Application application, float width, boolean autoEffectActivity) {
        appDisplayMetrics = application.getResources().getDisplayMetrics();
        WIDTH = width;
        if(autoEffectActivity){
            registerActivityLifecycleCallbacks(application);
        }
        if (appDensity == 0) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }
    }



    public static void setDefault(Activity activity) {
        float targetDensity = 0;
        try {
            targetDensity = appDisplayMetrics.widthPixels / WIDTH;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         *
         * 只修改Activity的density值
         */

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 主要用与WindowManger悬浮窗的ui适配
     * @param context
     */
    public static void setOtherScreenAdapter(@Nullable Context context) {

        float targetDensity = 0;
        try {
            targetDensity = appDisplayMetrics.widthPixels / WIDTH;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        float targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        DisplayMetrics activityDisplayMetrics = context.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }


    private static void registerActivityLifecycleCallbacks(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setDefault(activity);
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
            }
            @Override
            public void onActivityPaused(Activity activity) {
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });

    }
}

