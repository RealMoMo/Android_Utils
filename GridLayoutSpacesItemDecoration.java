package com.newline.ucos.launcher.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Realmo
 * @version 1.0.0
 * @name android-doorway
 * @email momo.weiye@gmail.com
 * @time 2020/10/13 14:20
 * @describe GridLayout上下间距的itemdecoration
 */
public class GridLayoutSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int rowCount;

    /**
     *
     * @param space  间距px
     * @param rowCount 每行的item个数
     */
    public GridLayoutSpacesItemDecoration(int space,int rowCount) {
        this.space = space;
        this.rowCount = rowCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //若为最后一行则不用加间距
        if(parent.getChildAdapterPosition(view)+rowCount>=parent.getAdapter().getItemCount()){
            return;
        }

        outRect.bottom = space;

    }

}
