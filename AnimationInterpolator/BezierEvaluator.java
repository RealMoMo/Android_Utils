package com.realmo.utils;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Point;

/**
 * @author Realmo
 * @version 1.0.0
 * @name BezierEvaluator
 * @email momo.weiye@gmail.com
 * @time 2018/1/3 13:52
 * @describe  自定义贝塞尔曲线动画估值器
 */
public class BezierEvaluator implements TypeEvaluator<Point> {

    //控制点
    private Point controllPoint;

    private Point curPoint;

    // 设置PathAnimation控制点的x/y阈值
    private  int thresholdX = 100;
    private int thresholdY = 200;

    public BezierEvaluator(Context context){
        curPoint = new Point();
        thresholdX = (int)(context.getResources().getDisplayMetrics().widthPixels * 0.1f);
        thresholdY = (int)(context.getResources().getDisplayMetrics().heightPixels * 0.1f);
    }

    public BezierEvaluator(Context context,Point controllPoint){
        this(context);
        this.controllPoint = controllPoint;


    }

    public void setControllPoint(Point controllPoint) {
        this.controllPoint = controllPoint;
    }

    public void setControllPoint(int x,int y){
        if(controllPoint == null){
            controllPoint = new Point();
        }
        controllPoint.set(x,y);
    }


    public void setControllPoint(int x1,int y1,int x2,int y2){
        int x = Math.abs(x1-x2);
        int y = Math.abs(y1-y2);

        if(x>y){
            if(y1 < thresholdY  && y2 < thresholdY ){
                y = Math.max(y1,y2)+thresholdY;
            }else if(y2<thresholdY){
                y= y1+thresholdY;
            }else if(y1<thresholdY){
                y= y2+thresholdY;
            }else{
                y = Math.min(y1,y2)-thresholdY;
            }
            x = (x1+x2)/2;
        }else{
            if(x1 - thresholdX <0 && x2 - thresholdX < 0){
                x = Math.max(x1,x2)+thresholdX;
            }else if(x2<thresholdX){
                x= x1+thresholdX;
            }else if(x1<thresholdX){
                x= x2+thresholdX;
            }else{
                x = Math.min(x1,x2)-thresholdX;
            }
            y = (y1+y2)/2;
        }
        if(controllPoint == null){
            controllPoint = new Point();
        }
        controllPoint.set(x,y);
    }

    @Override
    public Point evaluate(float t, Point startValue, Point endValue) {
        int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
        int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
        curPoint.set(x, y);
        return curPoint;
    }
}
