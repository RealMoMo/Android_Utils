package com.realmo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.realmo.customview.model.PieData;

import java.util.List;


/**
 * 饼图
 * 
 */

public class PieChart extends View {
    /**
     * 自定义颜色
     */
    private static int[] mColors = {
            0xFF323232,
            0xFFCFB054,
            0xFFDA8A8A,
            0xFFC89FC2,
            0xFF8AD3D2,
            0xFF7DB3CB,
            0xFF78899E,
            0xFF62AD98,
            0xFFB8A690,
            0xFFA1B060,
            0xFFD08E76
    };
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 饼状图宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图起始角度
     */
    private float mStartAngle = 0f;
    /**
     * 用户数据
     */
    private List<PieData> mData;
    /**
     * 动画时间
     */
    private static final long ANIMATION_DURATION = 1000;
    /**
     * 自定义动画
     */
    private PieChartAnimation mAnimation;
    /**
     * 绘制方式
     */
    private int mDrawWay = PART;
    public static final int PART = 0;//分布绘制
    public static final int COUNT = 1;//连续绘制

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防止抖动
        mPaint.setStyle(Paint.Style.FILL);//画笔为填充
        //初始化动画
        mAnimation = new PieChartAnimation();
        mAnimation.setDuration(ANIMATION_DURATION);
    }

    /**
     * 设置起始角度
     *
     * @param mStartAngle
     */
    public void setmStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle;
        invalidate();//刷新
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    public void setData(List<PieData> mData) {
        setmData(mData);
    }

    /**
     * 设置数据和绘制方式
     *
     * @param mData
     */
    public void setData(List<PieData> mData, int mDrawWay) {
        setmData(mData);
        this.mDrawWay = mDrawWay;
    }

    /**
     * 设置数据
     *
     * @param mData
     */
    private void setmData(List<PieData> mData) {
        sumValue = 0;
        this.mData = mData;
        initData(mData);
        startAnimation(mAnimation);
        invalidate();
    }

    float sumValue = 0;//数据值的总和

    /**
     * 初始化数据
     *
     * @param mData
     */
    private void initData(List<PieData> mData) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        /**
         * 计算数据总和确定颜色
         */
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            sumValue += data.getValue();
            int j = i % mColors.length;//设置颜色
            data.setColor(mColors[j]);
        }
        /**
         * 计算百分比和角度
         */
        float currentStartAngle = mStartAngle;
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            data.setCurrentStartAngle(currentStartAngle);
            //通过总和来计算百分比
            float percentage = data.getValue() / sumValue;
            //通过百分比来计算对应的角度
            float angle = percentage * 360;
            //设置用户数据
            data.setPercentage(percentage);
            data.setAngle(angle);
            currentStartAngle += angle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mData == null) {
            return;
        }
        //1.移动画布到中心点
        canvas.translate(mWidth / 2, mHeight / 2);
        //2.设置当前起始角度
        float currentStartAngle = mStartAngle;
        //3.确定饼图的半径
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.95);
        float r1 = r / 2;
        float r2 = r / 1.8f;
        //4.确定饼图的矩形大小
        RectF rectF = new RectF(-r, -r, r, r);
        RectF rectF1 = new RectF(-r1, -r1, r1, r1);
        RectF rectF2 = new RectF(-r2, -r2, r2, r2);
        for (int i = 0; i < mData.size(); i++) {
            PieData data = mData.get(i);
            //5.设置颜色
            mPaint.setColor(data.getColor());
            //6.绘制饼图
            if (mDrawWay == PART) {
                canvas.drawArc(rectF, data.getCurrentStartAngle(), data.getAngle(), true, mPaint);
            } else if (mDrawWay == COUNT) {
                canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
                //7.绘制下一块扇形时先将角度加上当前扇形的角度
                currentStartAngle += data.getAngle();
            }
        }
        //绘制中心空白处
        mPaint.setColor(0x33000000);
        canvas.drawArc(rectF2, currentStartAngle, 360f, true, mPaint);
        //绘制中心阴影部分
        mPaint.setColor(0xEEFFFFFF);
        canvas.drawArc(rectF1, currentStartAngle, 360f, true, mPaint);
        //绘制文字
        mPaint.setColor(0xEEFF4567);
        mPaint.setTextSize(80);
        String str = "印象丶亮仔";
        Rect textRect = new Rect();
        mPaint.getTextBounds(str, 0, str.length(), textRect);
        canvas.drawText("印象丶亮仔", -textRect.width() / 2, 0, mPaint);
    }

    /**
     * 自定义动画
     */
    public class PieChartAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                for (int i = 0; i < mData.size(); i++) {
                    PieData data = mData.get(i);
                    //通过总和来计算百分比
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    //根据插入时间来计算角度
                    angle = angle * interpolatedTime;
                    data.setAngle(angle);
                }
            } else {//默认显示效果
                for (int i = 0; i < mData.size(); i++) {
                    //通过总和来计算百分比
                    PieData data = mData.get(i);
                    float percentage = data.getValue() / sumValue;
                    //通过百分比来计算对应的角度
                    float angle = percentage * 360;
                    data.setPercentage(percentage);
                    data.setAngle(angle);
                }
            }
            invalidate();
        }
    }
}