package com.realmo.utils;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Point;

/**
 * @author Realmo
 * @version 1.0.0
 * @name LinePathEvaluator
 * @email momo.weiye@gmail.com
 * @time 2018/1/3 13:52
 * @describe  直线路径动画估值器
 */
public class LinePathEvaluator implements TypeEvaluator<Point> {


    private Point curPoint;

    public LinePathEvaluator(){
        curPoint = new Point();

    }




    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((endValue.x-startValue.x)*t+startValue.x);
        int y = (int) ((endValue.y-startValue.y)*t+startValue.y);
        curPoint.set(x, y);
        return curPoint;
    }
}
