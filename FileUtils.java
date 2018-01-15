package com.realmo.utils;

import java.io.File;

/**
 * @author Realmo
 * @version 1.0.0
 * @name FileUtils
 * @email momo.weiye@gmail.com
 * @time 2018/1/8 10:47
 * @describe
 */

public class FileUtils {

    /**
     * @param path
     * @return 文件大小
     */
    public static long getFileSize(String path) {
        long size = 0;
        File file = new File(path);
        if (file.exists()) {
            size = file.length();
        }

        return size;
    }

    /**
     *  delete file
     * @param path
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    /**
     * delete path
     * @param file
     */
    public static void deleteFile(File file) {
        if(file.exists()){
            if (file.isFile()) {
                file.delete();
                return;
            }

            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    file.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deleteFile(childFiles[i]);
                }
                file.delete();
            }
        }

    }

}
