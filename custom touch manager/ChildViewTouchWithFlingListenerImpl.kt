package com.newline.ucos.launcher.manager.touch

import android.content.Context
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Scroller

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:46
 * @describe
 */
class ChildViewTouchWithFlingListenerImpl(context: Context, touchEvent: ViewTouchEvent, view: View) :
    ChildViewTouchListenerImpl(context, touchEvent, view) {

    private val velocityTracker : VelocityTracker
    private val scroller: Scroller
    init {
        velocityTracker = VelocityTracker.obtain()
        scroller = Scroller(context)
    }

    override fun onDown(e: MotionEvent): Boolean {
        if(!scroller.isFinished){
            scroller.forceFinished(true)
        }
        velocityTracker.addMovement(e)
        return super.onDown(e)
    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {

        velocityTracker.computeCurrentVelocity(200)

        touchEvent!!.onFling(view!!,currentX,currentY,velocityTracker.xVelocity,velocityTracker.yVelocity)

        velocityTracker.clear()

        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        super.onScroll(e1, e2, distanceX, distanceY)
        velocityTracker.addMovement(e2)
        return false
    }

    override fun releaseRes() {
        super.releaseRes()
        velocityTracker.recycle()
    }


}
