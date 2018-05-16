package com.realmo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 代码截屏工具 修改
 *
 * 
 * //保存后发送广播 弹出提示
 * private  void saveSuccess(File file){
 * Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
 * Uri uri = Uri.fromFile(file);
 * intent.setData(uri);
 * sendBroadcast(intent);
 * ToastUtil.initToast(this, "已保存");
 * }
 *
 * 
 */

public class ScreenshotUtils {

    // 程序入口 截取当前屏幕
    public static File shootLoacleView(Activity a, String picpath) {
        return ScreenshotUtils.savePic(ScreenshotUtils.takeScreenShot(a), picpath);
    }

    // 程序入口 截取ScrollView
    public static File shootScrollView(ScrollView scrollView, String picpath) {
        return ScreenshotUtils.savePic(getScrollViewBitmap(scrollView, picpath), picpath);
    }

    // 程序入口 截取ListView
    public static File shootListView(ListView listView, String picpath) {
        return ScreenshotUtils.savePic(getListViewBitmap(listView, picpath), picpath);
    }

    // 程序入口 截取ListView
    public static File shootRecyclerView(RecyclerView recyclerView, String picpath, int rawNum) {
        return ScreenshotUtils.savePic(getRecyclerViewBitmap(recyclerView, picpath, rawNum), picpath);
    }

    //获取默认存储地址  参数要传空
    private static String getDefaultPath() {
        SimpleDateFormat sdf24H = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String format = sdf24H.format(new Date(System.currentTimeMillis()));
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/img/" + format + ".png";

    }


    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    // 保存到sdcard
    public static File savePic(Bitmap b, String strFileName) {
        File file = new File(TextUtils.isEmpty(strFileName) ? getDefaultPath() : strFileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 截取scrollview的屏幕    默认
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        Log.d(TAG, "实际高度:" + h);
        Log.d(TAG, " 高度:" + scrollView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {

            out = new FileOutputStream(TextUtils.isEmpty(picpath) ? getDefaultPath() : picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    private static String TAG = "Listview and ScrollView item 截图:";

    /**
     * 截图listview
     **/
    public static Bitmap getListViewBitmap(ListView listView, String picpath) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        Log.e(TAG, "实际高度:" + h);
        Log.e(TAG, "list 高度:" + listView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(TextUtils.isEmpty(picpath) ? getDefaultPath() : picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;
    }

    private static Bitmap getRecyclerViewBitmap(RecyclerView recyclerView, String picpath, int rowNum) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < recyclerView.getChildCount() / rowNum; i++) {
            h += recyclerView.getChildAt(i).getHeight();
        }
        Log.e(TAG, "实际高度:" + h);
        Log.e(TAG, "list 高度:" + recyclerView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(recyclerView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        recyclerView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(TextUtils.isEmpty(picpath) ? getDefaultPath() : picpath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
        return bitmap;

    }
}
