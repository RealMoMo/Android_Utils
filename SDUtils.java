package momo.com.week12_project.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by realmo on 2016/6/5 0005.
 */
public class SDUtils {
    /**
     * 保存文件到SD卡中
     *
     * @param in       网络文件输入流
     * @param fileName 要保存的文件名
     * @param filePath 文件保存在哪个目录,不包含SD卡跟路径
     */
    public static void saveFile(InputStream in, String fileName, String filePath) throws Exception {
        //先判断SD卡是否可用
        if (!isMount()) {
            Log.d("realmo", "================SD卡不可用===========");
            return;
        }
        String path;
        //获取SDcart路径
        if (filePath == null) {
            //保存到SDcard根目录
            path = getSDcardPath();
        } else {
            path = getSDcardPath() + filePath;
        }
        //创建文件
        File file = new File(path);
        //如果目录文件不存在，则创建一个
        if (!file.exists()) {
            file.mkdirs();
        } else {
            //目录文件已经存在，但如果不是目录，则直接返回
            if (!file.isDirectory()) {
                Log.d("realmo", "============保存的路径不是有效的目录============");
                return;
            }
        }

        //创建文件
        File newFile = new File(file, fileName);
        OutputStream out = new FileOutputStream(newFile);
        byte[] buffer = new byte[2048];
        int tmp = 0;
        while ((tmp = in.read(buffer)) != -1) {
            out.write(buffer, 0, tmp);
        }
        out.flush();
        out.close();
        in.close();
    }

    /**
     * 保存文件到SD卡中
     *
     * @param arr      保存的字节数组
     * @param fileName 要保存的文件名
     * @param filePath 文件保存在哪个目录,不包含SD卡跟路径
     * @return 返回保存后的文件名及路径
     * @throws Exception
     */
    public static String saveFile(byte[] arr, String fileName, String filePath) throws Exception {
        //先判断SD卡是否可用
        if (!isMount()) {
            Log.d("realmo", "================SD卡不可用===========");
            return null;
        }
        String path;
        //获取SDcart路径
        if (filePath == null) {
            //保存到SDcard根目录
            path = getSDcardPath();
        } else {
            path = getSDcardPath() + filePath;
        }
        //创建文件
        File file = new File(path);
        //如果目录文件不存在，则创建一个
        if (!file.exists()) {
            file.mkdir();
        } else {
            //目录文件已经存在，但如果不是目录，则直接返回
            if (!file.isDirectory()) {
                Log.d("realmo", "============保存的路径不是有效的目录============");
                return null;
            }
        }

        //创建文件
        File newFile = new File(file, fileName);
        String newPath = newFile.getAbsolutePath();
        OutputStream out = new FileOutputStream(newFile);
        out.write(arr, 0, arr.length);
        out.flush();
        out.close();
        return newPath;
    }

    /**
     * 判断SD卡是否挂载
     *
     * @return
     */
    public static boolean isMount() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径，后面加上分隔符File.separator:/
     *
     * @return
     */
    public static String getSDcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }
}
