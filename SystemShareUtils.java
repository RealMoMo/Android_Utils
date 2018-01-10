package com.realmo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Realmo
 * @version 1.0.0
 * @name SystemShareUtils
 * @email momo.weiye@gmail.com
 * @time 2018/1/5 14:43
 * @describe 调用系统的分享功能(请确保传入的filePath exist)
 */

public class SystemShareUtils {


//    //传文字
//    public static void shareString(Context context,String title,String str) {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, str);
//        shareIntent.setType("text/plain");
//        //设置分享列表的标题，并且每次都显示分享列表
//        context.startActivity(Intent.createChooser(shareIntent, title));
//    }

    //传文本文件
    public static void shareTextFile(Context context,String title,String filePath) {
        //由文件得到uri
        Uri fileUri = Uri.fromFile(new File(filePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("*/*");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    //传单图
    public static void shareSingleImage(Context context,String title,String imagePath) {
        //由文件得到uri
        Uri imageUri = Uri.fromFile(new File(imagePath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, "分享到"));

    }

//    //传多图
//    public static void shareMoreImage(Context context,String title,String...paths) {
//
//        ArrayList<Uri> uriList = new ArrayList<>();
//
//        //demo code
////        String path = Environment.getExternalStorageDirectory() + File.separator;
////        uriList.add(Uri.fromFile(new File(path+"head.jpg")));
////        uriList.add(Uri.fromFile(new File(path+"test.jpg")));
//
//        for(String path:paths){
//            uriList.add(Uri.fromFile(new File(path)));
//        }
//
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
//        shareIntent.setType("image/*");
//        context.startActivity(Intent.createChooser(shareIntent, title));
//    }

    //传多图
    public static void shareMoreImage(Context context,int titleResId,String...paths) {

        ArrayList<Uri> uriList = new ArrayList<>();

        //demo code
//        String path = Environment.getExternalStorageDirectory() + File.separator;
//        uriList.add(Uri.fromFile(new File(path+"head.jpg")));
//        uriList.add(Uri.fromFile(new File(path+"test.jpg")));

        for(String path:paths){
            uriList.add(Uri.fromFile(new File(path)));
        }

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(titleResId)));
    }

    //传多图
    public static void shareMoreImage(Context context, int titleResId, ArrayList<Uri> uriList) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        shareIntent.setType("image/*");
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(titleResId)));
    }




    //传zip
    public static void shareZip(Context context,String title,String zipPath) {
        //由文件得到uri
        Uri fileUri = Uri.fromFile(new File(zipPath));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.setType("*/*");
        context.startActivity(Intent.createChooser(shareIntent, title));

    }
}
