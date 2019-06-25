package com.hht.baselib.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Realmo
 * @version 1.0.0
 * @name
 * @email momo.weiye@gmail.com
 * @time 2019/6/24 9:39
 * @describe
 */
public class ToastUtils {

    private static Toast mToast;

    public static void toast(Context context,String text,int duration){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context,text,duration);
        mToast.show();
    }


    public static void toast(Context context,int textId,int duration){
        if(mToast != null){
            mToast.cancel();
        }
        mToast = Toast.makeText(context,context.getText(textId),duration);
        mToast.show();
    }



}
