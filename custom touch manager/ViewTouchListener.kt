package com.newline.ucos.launcher.manager.touch

import android.view.GestureDetector
import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:29
 * @describe
 */
interface ViewTouchListener : View.OnTouchListener, GestureDetector.OnGestureListener {


    fun releaseRes()
}
