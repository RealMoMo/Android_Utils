package com.newline.ucos.launcher.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Realmo
 * @version 1.0.0
 * @name android-doorway
 * @email momo.weiye@gmail.com
 * @time 2020/9/8 14:52
 * @describe LinearLayout 上下/左右间距的itemdecoration
 */
public class LinearLayoutSpacesItemDecoration extends RecyclerView.ItemDecoration{

    int orientation;
    //间隔 px
    private int space;
    //首条头部是否生效间距
    private boolean effectFirstTopSpace;

    public LinearLayoutSpacesItemDecoration(int orientation, int space) {
        this(orientation,space,false);
    }

    public LinearLayoutSpacesItemDecoration(int orientation, int space,boolean effectFirstTopSpace) {
        this.orientation = orientation;
        this.space = space;
        this.effectFirstTopSpace = effectFirstTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if(orientation == LinearLayoutManager.HORIZONTAL){
            if (parent.getChildPosition(view) != 0){
                outRect.left = space;
            }
        }else{
            if (parent.getChildPosition(view) != 0){
                outRect.top = space;
            }else{
                if(effectFirstTopSpace){
                    outRect.top = space;
                }
            }
        }


    }
}
