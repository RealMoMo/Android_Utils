package com.realmo.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Realmo
 * @version 1.0.0
 * @email momo.weiye@gmail.com
 * @time 2018/6/1 10:04
 * @describe
 */
public final class CloseUtils {

    /**
     * 关闭Closeable对象
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
