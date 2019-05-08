package com.hht.oem.quicksetting.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class SystemPropertiesUtils {

    public static void setProperty(String key, String value) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("set", String.class, String.class);
            method.invoke(clazz, key, value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param key 关键字
     * @param def 根据默认字 获取
     * @return
     */
    public static Object getProperty(String key, Object def) {
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            if (def instanceof String) {
                String value = (String) def;
                Method method = clazz.getMethod("get", String.class, String.class);
                return method.invoke(clazz, key, value);
            }
            else if (def instanceof Boolean) {
                Boolean value = (Boolean) def;
                Method method = clazz.getMethod("getBoolean", String.class, boolean.class);
                return method.invoke(clazz, key, value);
            }
            else if (def instanceof Integer) {
                Integer value = (Integer) def;
                Method method = clazz.getMethod("getInt", String.class, int.class);
                return method.invoke(clazz, key, value);
            }
            else if (def instanceof Long) {
                Long value = (Long) def;
                Method method = clazz.getMethod("getLong", String.class, long.class);
                return method.invoke(clazz, key, value);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (def instanceof String) {
            return "";
        }
        else if (def instanceof Boolean) {
            return false;
        }
        else if (def instanceof Long) {
            return -1L;
        }
        else if (def instanceof Integer) {
            return -1;
        }
        return new Object();
    }
}
