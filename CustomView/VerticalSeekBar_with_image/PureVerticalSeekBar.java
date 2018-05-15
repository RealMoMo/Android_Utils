package com.hht.rsquicksetting.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hht.rsquicksetting.R;


/**
 * @author Realmo
 * @version 1.0.0
 * @name PureVerticalSeekBar
 * @email momo.weiye@gmail.com
 * @time 2018/4/21 14:35
 * @describe
 */
public class PureVerticalSeekBar extends View {
    private static final String TAG = PureVerticalSeekBar.class.getSimpleName();
    private int startColor = Color.GRAY;
    private int middleColor = Color.GRAY;
    private int endColor = Color.GRAY;
    private int thumbColor = Color.BLACK;
    private int thumbBorderColor = Color.TRANSPARENT;
    private int colorArray[] = {startColor, middleColor, endColor};
    private int colorArray2[] = {Color.GRAY, Color.GRAY, Color.GRAY};
    private float x, y;
    private float mRadius;
    private float progress;
    private float maxCount = 100f;
    private float sLeft, sTop, sRight, sBottom;
    private float sWidth, sHeight;
    private LinearGradient linearGradient;
    private Paint paint = new Paint();
    protected OnVerticalSeekBarChangeListener onSlideChangeListener;

    private static final int DEFAULT_CIRCLE_RADIUS = 0;
    private static final int DEFAULT_CIRCLE_COLOR = Color.GRAY;

    private int circle_radius = DEFAULT_CIRCLE_RADIUS;

    private int vertical_color = Color.GRAY;
    private int thumbResId = 0;
    private int progressBarId = 0;
    private Bitmap bitmapThumb;
    private Bitmap bitmapBar;
    private boolean dragable = true;

    private boolean fromUser = false;


    public PureVerticalSeekBar(Context context) {
        this(context, null);
    }

    public PureVerticalSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PureVerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context,attrs,defStyle);

    }

    private void init(Context context, AttributeSet attrs, int defStyle){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PureVerticalSeekBar, defStyle, 0);

        circle_radius = a.getDimensionPixelSize(R.styleable.PureVerticalSeekBar_circle_radius, DEFAULT_CIRCLE_RADIUS);
        thumbColor = a.getColor(R.styleable.PureVerticalSeekBar_circle_color, DEFAULT_CIRCLE_COLOR);
        dragable = a.getBoolean(R.styleable.PureVerticalSeekBar_dragable, true);
        vertical_color = a.getColor(R.styleable.PureVerticalSeekBar_vertical_color, Color.GRAY);
        thumbResId = a.getResourceId(R.styleable.PureVerticalSeekBar_image_thumb, 0);
        progressBarId = a.getResourceId(R.styleable.PureVerticalSeekBar_image_progressbar,0);
        a.recycle();
        setCircle_color(thumbColor);
        setVertical_color(vertical_color);

        if(thumbResId != 0 && bitmapThumb == null){
            bitmapThumb = BitmapFactory.decodeResource(getResources(), thumbResId);
        }

        if(progressBarId != 0 && bitmapBar == null){
            bitmapBar = BitmapFactory.decodeResource(getResources(), progressBarId);
        }
    }

    /**
     * @param circle_color 滑块的颜色，在没有设置背景图情况下生效
     */
    public void setCircle_color(int circle_color) {
        this.thumbColor = circle_color;
        invalidate();
    }

    /**
     * @param dragable 设置是否可以拖动
     */
    public void setDragable(boolean dragable) {
        this.dragable = dragable;
        invalidate();
    }

    /**
     * @param vertical_color 滑竿的颜色
     */
    public void setVertical_color(int vertical_color) {
        this.vertical_color = vertical_color;
        this.startColor = vertical_color;
        this.middleColor = vertical_color;
        this.endColor = vertical_color;
        colorArray[0] = startColor;
        colorArray[1] = middleColor;
        colorArray[2] = endColor;
        colorArray2[0] = startColor;
        colorArray2[1] = middleColor;
        colorArray2[2] = endColor;
        invalidate();
    }

    /**
     * @param vertical_top_color 滑竿上部分的颜色
     * @param vertical_bom_color 滑竿下部分的颜色
     */
    public void setVertical_color(int vertical_top_color, int vertical_bom_color) {
        colorArray[0] = vertical_bom_color;
        colorArray[1] = vertical_bom_color;
        colorArray[2] = vertical_bom_color;
        colorArray2[0] = vertical_top_color;
        colorArray2[1] = vertical_top_color;
        colorArray2[2] = vertical_top_color;
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setColor(int startColor, int middleColor, int endColor, int thumbColor, int thumbBorderColor) {
        this.startColor = startColor;
        this.middleColor = middleColor;
        this.endColor = endColor;
        this.thumbColor = thumbColor;
        this.thumbBorderColor = thumbBorderColor;
        colorArray[0] = startColor;
        colorArray[1] = middleColor;
        colorArray[2] = endColor;
        invalidate();
    }


    /**
     * @param verticalColor    滑竿的颜色
     * @param thumbColor       滑块的颜色，在没有设置背景图情况下生效
     * @param thumbBorderColor 滑竿边缘背景色，默认透明
     */
    public void setColor(int verticalColor, int thumbColor, int thumbBorderColor) {
        this.startColor = verticalColor;
        this.middleColor = verticalColor;
        this.endColor = verticalColor;
        this.thumbColor = thumbColor;
        this.thumbBorderColor = thumbBorderColor;
        colorArray[0] = startColor;
        colorArray[1] = middleColor;
        colorArray[2] = endColor;
        invalidate();
    }

    /**
     * @param verticalColor 滑竿的颜色
     * @param thumbColor    滑块的颜色，在没有设置背景图情况下生效
     */
    public void setColor(int verticalColor, int thumbColor) {
        this.startColor = verticalColor;
        this.middleColor = verticalColor;
        this.endColor = verticalColor;
        this.thumbColor = thumbColor;
        colorArray[0] = startColor;
        colorArray[1] = middleColor;
        colorArray[2] = endColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int h = getMeasuredHeight();
        int w = getMeasuredWidth();

        if (thumbResId == 0) {
            if (circle_radius == 0) {
                mRadius = (float) w / 2;
            } else {
                mRadius = circle_radius;
            }
            sLeft = w * 0.25f;
            sRight = w * 0.75f;
            sTop = 0;
            sBottom = h;
            sWidth = sRight - sLeft;
            sHeight = sBottom - sTop;
            x = (float) w / 2;
            y = (float) (1 - 0.01 * progress) * sHeight;
        } else {

            int outH = (int) (bitmapThumb.getHeight() * getMeasuredWidth() / bitmapThumb.getWidth());
            if (circle_radius == 0) {
                mRadius = (float) outH / 2;
            } else {
                mRadius = outH / 2;
            }
            sLeft = w * 0.25f;
            sRight = w * 0.75f;
            sTop = 0;
            sBottom = h;
            sWidth = sRight - sLeft;
            sHeight = sBottom - sTop;
            x = (float) w / 2;
            y = (float) (1 - 0.01 * progress) * sHeight;
        }

        drawProgressBar(canvas);
        drawThumb(canvas);
        paint.reset();
    }

    private void drawProgressBar(Canvas canvas) {
        Rect rect = new Rect((getMeasuredWidth()-bitmapBar.getWidth())/2, 0, (getMeasuredWidth()-bitmapBar.getWidth())/2+bitmapBar.getWidth(), getMeasuredHeight());
        canvas.drawBitmap(bitmapBar, null, rect, null);
    }

    private void drawThumb(Canvas canvas) {


        y = y < mRadius ? mRadius : y;
        y = y > sHeight - mRadius ? sHeight - mRadius : y;

        if (thumbResId == 0) {
            Paint thumbPaint = new Paint();
            thumbPaint.setAntiAlias(true);
            thumbPaint.setStyle(Paint.Style.FILL);
            thumbPaint.setColor(thumbColor);
            canvas.drawCircle(x, y, mRadius, thumbPaint);
        } else {

            Rect rect = new Rect(0, (int) (y - bitmapThumb.getHeight() / 2), getMeasuredWidth(), (int) (y + bitmapThumb.getHeight() / 2));
            canvas.drawBitmap(bitmapThumb, null, rect, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!dragable) {
            return true;
        }
        this.y = event.getY();
        progress = (sHeight - y) / sHeight * 100;

        onSlideProgress(event, progress);

        return true;
    }

    public interface OnVerticalSeekBarChangeListener {
        void onSeekBarChangeListener(View view, int progress,boolean fromUser);

        void onStopTrackingTouch(View view);

        void onStartTrackingTouch(View view);
    }

    public void setOnProgressChangeListener(OnVerticalSeekBarChangeListener onStateChangeListener) {
        this.onSlideChangeListener = onStateChangeListener;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        fromUser =false;
        if (onSlideChangeListener != null) {
            onSlideChangeListener.onSeekBarChangeListener(this, (int)progress,fromUser);
        }
        invalidate();
    }


    protected void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }


    public int getProgress(){
        return (int)progress;
    }

    public void onSlideProgress(MotionEvent event, float progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fromUser = true;
                if (onSlideChangeListener != null) {
                    onSlideChangeListener.onStartTrackingTouch(this);
                }
                this.invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (onSlideChangeListener != null) {
                    onSlideChangeListener.onSeekBarChangeListener(this, (int)progress,fromUser);
                }
                setProgress(progress);
                this.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (onSlideChangeListener != null) {
                    onSlideChangeListener.onStopTrackingTouch(this);
                }
                break;
        }

    }


}
