package com.newline.ucos.launcher.manager.touch

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * @author Realmo
 * @version 1.0.0
 * @name FloatBarMenu
 * @email momo.weiye@gmail.com
 * @time 2019/9/9 9:32
 * @describe
 */
abstract class BaseTouchListenerImpl(
        private var mContext: Context?,
        protected var touchEvent: ViewTouchEvent?,
        protected var view: View?
) : ViewTouchListener {
    protected var gestureDetector: GestureDetector

    protected var downX: Float = 0.toFloat()
    protected var downY: Float = 0.toFloat()
    protected var dx: Int = 0
    protected var dy: Int = 0
    protected var currentX: Int = 0
    protected var currentY: Int = 0


    init {
        view!!.setOnTouchListener(this)
        gestureDetector = GestureDetector(mContext, this)
    }

    override fun onDown(e: MotionEvent): Boolean {
        //maked selector click effect worked
        view!!.isPressed = true

        downX = e.x
        downY = e.y

        dx = e.rawX.toInt()
        dy = e.rawY.toInt()

        touchEvent!!.onTouchDown(view!!)
        return true
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {

        touchEvent!!.onTouchClick(view!!)
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        touchEvent!!.onTouchLongClick(view!!)
    }


    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                view!!.isPressed = false
                touchEvent!!.onTouchUp(view!!)
            }
            MotionEvent.ACTION_CANCEL -> {
                view!!.isPressed = false
                touchEvent!!.onTouchCancel(view!!)
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    override fun releaseRes() {
        mContext = null
        view = null
        touchEvent = null
    }
}
