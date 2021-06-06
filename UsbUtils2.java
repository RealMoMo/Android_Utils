package com.newline.initial.setting.utils;

import android.content.Context;
import android.os.storage.StorageManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Realmo
 * @version 1.0.0
 * @name NewlineInitialSetting
 * @email momo.weiye@gmail.com
 * @time 2020/4/10 15:23
 * @describe
 */
public class UsbUtils {


    public static Object[] getStoragePaths(Context context) {

        StorageManager storageManager = (StorageManager) context
                .getSystemService(Context.STORAGE_SERVICE);

        try {
            Class<?>[] paramClasses = {};
            Method getVolumeList = StorageManager.class.getMethod("getVolumeList", paramClasses);
            Object[] params = {};
            Object[] invokes = (Object[]) getVolumeList.invoke(storageManager, params);
            if (invokes != null) {
                return invokes;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param obj  实为StorageVolume对象
     * @return usb路径
     */
    public static  String getStoragePath(Object obj){
        try {
            Method getPath  = obj.getClass().getMethod("getPath", new Class[0]);
            return (String) getPath.invoke(obj, new Object[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否为自身的存储设备
     * @param path
     * @return
     */
    public static boolean isSelfStorage(String path){
        if("/storage/emulated/0".equals(path)){
            return true;
        }
        return false;
    }


    /**
     *
     * @param obj  实为StorageVolume对象
     * @return usb标签名
     */
    public static String getUsbDeviceLabel(Object obj){
        try {
            Method getUserLabel = obj.getClass().getMethod("getUserLabel",new Class[0]);
            return (String) getUserLabel.invoke(obj,new Object[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "Unknown";

    }
}
