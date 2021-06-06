package com.newline.ucos.launcher.manager.touch

import android.content.Context
import android.view.MotionEvent
import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:46
 * @describe
 */
open class LayoutViewTouchListenerImpl(context: Context, touchEvent: ViewTouchEvent, view: View) :
    BaseTouchListenerImpl(context, touchEvent, view) {

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        currentX = (e2.rawX - downX).toInt()
        currentY = (e2.rawY - downY).toInt()
        touchEvent!!.onTouchMoving(view!!, currentX, currentY,(e2.x-downX).toInt(),(e2.y-downY).toInt())
        return false
    }


}
