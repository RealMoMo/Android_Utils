package com.newline.screencast.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @name
 * @author Realmo
 * @email   momo.weiye@gmail.com
 * @version 1.0.0
 * @time 2019/10/11 9:08
 * @describe
 */
abstract class NavigationBaseFragment : Fragment() {


    private var rootLayout:View?=null
    private var needInitView = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(rootLayout ==null){
            rootLayout = inflater.inflate(getLayoutId(),container,false)

        }
        return rootLayout
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //因为用了navigation 框架是用的回退操作用的是replace
        if(needInitView){
            needInitView = false
            initView(view)
        }

    }


    override fun onDestroy() {
        super.onDestroy()

    }

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 加载布局
     */
    @LayoutRes
    abstract fun getLayoutId():Int

    /**
     * 初始化 View
     */
    abstract fun initView(view : View)




}