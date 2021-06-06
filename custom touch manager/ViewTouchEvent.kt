package com.newline.ucos.launcher.manager.touch

import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:35
 * @describe
 */
interface ViewTouchEvent {

    //按下事件
    fun onTouchDown(view: View)

    //抬起事件
    fun onTouchUp(view: View)

    //取消事件
    fun onTouchCancel(view: View)

    //点击事件
    fun onTouchClick(view: View)

    //长按事件
    fun onTouchLongClick(view: View)

    /**
     * 移动事件
     * @param parentX ：父控件对应在屏幕的x坐标
     * @param parentY ：父控件对应在屏幕的y坐标
     * @param dx :ACTION_DOWN时，至现在，x方向的变化量
     * @param dy :ACTION_DOWN时，至现在，y方向的变化量
     */
    fun onTouchMoving(view: View, parentX: Int, parentY: Int, dx:Int, dy:Int)


    /**
     * Fling事件
     * @param view : View
     * @param x ：屏幕的x坐标
     * @param y ：屏幕的y坐标
     * @param velocityX : x方向的加速度
     * @param velocityY: y方向的加速度
     */
    fun onFling(view : View,x:Int,y:Int,velocityX: Float,velocityY:Float)

}
