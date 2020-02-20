package com.hht.memory.optimization.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;


public class WaveView extends View {
    private Paint mPaintWavw;//波浪画笔
    private Paint mPaintProgress;//进度画笔
    private float mItemWaveLength;//波浪长度
    private float mRadius;//圆的半径
    private float mWaveHeight = 20;//波浪的高度
    private float mProgressTextSize = 130 ;//进度文字的大小
    private Path mPathWave;//波浪路径（不透明）
    private Path mPathWaveAlpha;//波浪路径（半透明）
    private Path mPathCircle;//圆形球路径
    private float mWave=0;//波浪偏移量（实现波浪效果的关键）
    private float mProgress = 0;//进度
    private Paint.FontMetricsInt mFontMetricsInt;
    private ObjectAnimator mWaveobjectAnimator;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        mPaintWavw = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintWavw.setColor(Color.parseColor("#33F92B"));
        mPaintWavw.setStyle(Paint.Style.FILL);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStrokeWidth(10);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);//设置笔帽为圆形
        mPaintProgress.setStrokeJoin(Paint.Join.ROUND);//设置拐角为圆形
        mPaintProgress.setColor(Color.WHITE);//将进度颜色设置为白色
        mPaintProgress.setTextAlign(Paint.Align.CENTER);

        mPathWave = new Path();
        mPathWaveAlpha = new Path();
        mPathCircle = new Path();
    }

    /**
     * 修改高度
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measuredHeight(heightMeasureSpec));
    }

    /**
     * 测量宽
     * @param widthMeasureSpec
     */
    private int measureWidth(int widthMeasureSpec) {
        int result ;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    /**
     * 测量高
     * @param heightMeasureSpec
     */
    private int measuredHeight(int heightMeasureSpec) {
        int result ;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }else{
            result = 200;
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(result,specSize);
            }
        }
        return  result;
    }

    /**
     * 当控件大小发生改变的时候对一些数值进行调整以适应控件大小
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (float) (Math.min(w, h)*0.9/2);
        mItemWaveLength = mRadius *2;//一段波浪的长度
        mPathCircle.addCircle(w/2,h/2,mRadius, Path.Direction.CW);
        mProgressTextSize = mRadius*0.7f;//当控件大小发生变化时，动态修改文字大小
        mPaintProgress.setTextSize(mProgressTextSize);
        mFontMetricsInt = mPaintProgress.getFontMetricsInt();
        mWaveHeight = mRadius/8;//当控件大小发生变化时，动态修改波浪高度
        invalidate();
        startWaveAnim();
    }

    private void startWaveAnim() {
        mWaveobjectAnimator = ObjectAnimator.ofFloat(this, "wave", 0, mItemWaveLength).setDuration(4000);
        mWaveobjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveobjectAnimator.setInterpolator(new LinearInterpolator());
        mWaveobjectAnimator.start();
    }

    /**
     * 设置波浪偏移量（波浪的动画效果核心是靠这个实现的）
     * @param wave
     */
    public void setWave(float wave){
        mWave = wave;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.clipPath(mPathCircle);//将画布裁剪为圆形
        canvas.drawColor(Color.parseColor("#3233F92B"));
        mPathWave.reset();
        mPathWaveAlpha.reset();
        //将波浪路径起始点移至圆形球的左侧一段波浪长度（即上下起伏为一段）的位置处
        mPathWave.moveTo(getWidth()/2-mRadius-mItemWaveLength+mWave,getHeight()/2+mRadius+mWaveHeight-mProgress*(mRadius*2+mWaveHeight*2));
        mPathWaveAlpha.moveTo(getWidth()/2-mRadius-mItemWaveLength+mWave+mItemWaveLength/8,getHeight()/2+mRadius+mWaveHeight-mProgress*(mRadius*2+mWaveHeight*2));

        float half = mItemWaveLength / 4;
        for(float x= -mItemWaveLength;x<getWidth()+mItemWaveLength;x+=mItemWaveLength){
            mPathWave.rQuadTo(half/2,-mWaveHeight,half,0);//贝赛尔曲线实现波浪
            mPathWave.rQuadTo(half/2,mWaveHeight,half,0);
            mPathWaveAlpha.rQuadTo(half/2,-mWaveHeight,half,0);//贝赛尔曲线实现波浪
            mPathWaveAlpha.rQuadTo(half/2,mWaveHeight,half,0);
        }
        mPathWave.lineTo(getWidth(),getHeight());
        mPathWave.lineTo(0,getHeight());
        mPathWave.close();//制造闭合路径
        mPathWaveAlpha.lineTo(getWidth(),getHeight());
        mPathWaveAlpha.lineTo(0,getHeight());
        mPathWaveAlpha.close();//制造闭合路径
        mPaintWavw.setColor(Color.parseColor("#7a33F92B"));//设置后面的波浪为半透明
        canvas.drawPath(mPathWaveAlpha, mPaintWavw);
        mPaintWavw.setColor(Color.parseColor("#33F92B"));//设置前面的波浪为不透明
        canvas.drawPath(mPathWave, mPaintWavw);
        canvas.drawText((int)(mProgress*100)+"%",getWidth()/2,getHeight()/2+((mFontMetricsInt.bottom-mFontMetricsInt.top)/2-mFontMetricsInt.bottom),mPaintProgress);
        canvas.restore();
    }

    /**
     * 设置进度（不带动画）
     * @param progress
     */
    public void setProgress(float progress){
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置进度带动画
     * @param progress
     */
    public void setProgressWithAnim(float progress){
        ObjectAnimator.ofFloat(this, "progress", 0, progress).setDuration(4000).start();
    }


    /**
     * 设置进度带动画
     * @param progress
     */
    public void setProgressWithAnim(float progress,long duration){
        ObjectAnimator.ofFloat(this, "progress", 0, progress).setDuration(duration).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mWaveobjectAnimator!=null){
            //当控件移除时取消动画
            mWaveobjectAnimator.cancel();
        }
    }
}
