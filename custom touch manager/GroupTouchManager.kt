package com.newline.ucos.launcher.manager.touch

import android.content.Context
import android.view.View

/**
 * @name android-doorway
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2020/9/9 11:20
 * @describe
 * 分组管理ViewTouch，主要便于 同组同类型的移除
 * 不建议与 [com.newline.ucos.launcher.manager.touch.TouchManager]共同使用
 */
class GroupTouchManager {

    private val touchManager : TouchManager
    private val groupTouchMap : MutableMap<String,MutableList<ViewTouchBean>>

    private constructor(){
        touchManager = TouchManager.instance
        touchManager.init()

        groupTouchMap = LinkedHashMap(4)
    }


    fun addChildViewTouchListener(context: Context, event: ViewTouchEvent, view: View, groupTag:String) {
        touchManager.addChildViewTouchListener(context, event, view)
        getKeyContent(groupTag).add(ViewTouchBean(view, TYPE_VIEW))

    }


    fun addLayoutViewTouchListener(context: Context, event: ViewTouchEvent, view: View, groupTag:String) {
        touchManager.addLayoutViewTouchListener(context, event, view)
        getKeyContent(groupTag).add(ViewTouchBean(view, TYPE_VIEWGROUP))

    }




    fun addChildViewTouchWithFlingListener(context: Context, event: ViewTouchEvent, view: View, groupTag:String) {
        touchManager.addChildViewTouchWithFlingListener(context, event, view)
        getKeyContent(groupTag).add(ViewTouchBean(view, TYPE_VIEW_WITH_FLING))
        touchManager.let {  }
    }


    fun addLayoutViewTouchWithFlingListener(context: Context, event: ViewTouchEvent, view: View, groupTag:String) {

        touchManager.addLayoutViewTouchWithFlingListener(context, event, view)
        getKeyContent(groupTag).add(ViewTouchBean(view, TYPE_VIEWGROUP_WITH_FLING))
    }


    fun removeChildViewTouchListener(groupTag: String){
        removeView(groupTag, TYPE_VIEW){
            touchManager.removeChildViewTouchFlingListener(it)
        }

    }

    fun removeChildViewTouchFlingListener(groupTag: String){
        removeView(groupTag, TYPE_VIEW_WITH_FLING){
            touchManager.removeChildViewTouchFlingListener(it)
        }

    }

    fun removeLayoutViewTouchListener(groupTag: String){
        removeView(groupTag, TYPE_VIEWGROUP){
            touchManager.removeChildViewTouchFlingListener(it)
        }
    }

    fun removeLayoutViewTouchFlingListener(groupTag: String){
        removeView(groupTag, TYPE_VIEWGROUP_WITH_FLING){
            touchManager.removeChildViewTouchFlingListener(it)
        }
    }

    private fun removeView(groupTag: String,touchType:Int,method:(View)->Unit){
        groupTouchMap.get(groupTag)?.let {

            val mIterator = it.iterator()
            while (mIterator.hasNext()) {
                val next = mIterator.next()
                if (touchType==next.type) {
                    method(next.view!!)
                    mIterator.remove()
                    next.view = null
                }
            }

        }
    }

    private fun getKeyContent(key:String):MutableList<ViewTouchBean>{
        groupTouchMap.get(key)?.let {
            return it
        }
        val list = ArrayList<ViewTouchBean>(8)
        groupTouchMap.put(key,list)
        return list
    }

    companion object {

        private const val TYPE_VIEW = 0
        private const val TYPE_VIEW_WITH_FLING = TYPE_VIEW+1
        private const val TYPE_VIEWGROUP = TYPE_VIEW+2
        private const val TYPE_VIEWGROUP_WITH_FLING = TYPE_VIEW+3

        private var mInstance: GroupTouchManager? = null

        val instance: GroupTouchManager
            get() {
                if (mInstance == null) {
                    synchronized(TouchManager::class.java) {
                        if (mInstance == null) {
                            mInstance = GroupTouchManager()
                        }
                    }
                }
                return mInstance!!
            }
    }


    private data class ViewTouchBean(var view:View?,val type:Int)
}