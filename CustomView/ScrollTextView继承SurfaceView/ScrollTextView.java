package com.realmo.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.realmo.utils.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScrollTextView extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "realmo";
    private SurfaceHolder surfaceHolder;   //providing access and control over this SurfaceView's underlying surface.
    private Paint paint = null;
    private boolean bStop = false;          // stop scroll
    //Default value
    private boolean clickEnable = false;    // click to stop/start
    public boolean isHorizontal = true;    // horizontal｜V
    private int speed = 1;                  // scroll-speed
    private String text = "";               // scroll text
    private float textSize = 15f;           // text size
    private int textColor = Color.BLACK;    // text color

    private int defScrollTimes = Integer.MAX_VALUE;  // scroll XX times default,
    private int needScrollTimes = 0;        //scroll times

    private int viewWidth = 0;
    private int viewHeight = 0;
    private float textWidth = 0f;
    private float density = 1;
    private float textX = 0f;
    private float textY = 0f;
    private float viewWidth_plus_textLength = 0.0f;
    private ScheduledExecutorService scheduledExecutorService;
    private int scheduleTime = 10;

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollText);
        clickEnable = arr.getBoolean(R.styleable.ScrollText_clickEnable, clickEnable);
        isHorizontal = arr.getBoolean(R.styleable.ScrollText_isHorizontal, isHorizontal);
        speed = arr.getInteger(R.styleable.ScrollText_speed, speed);
        text = arr.getString(R.styleable.ScrollText_text);
        textColor = arr.getColor(R.styleable.ScrollText_text_color, textColor);
        textSize = arr.getDimension(R.styleable.ScrollText_text_size, textSize);
        defScrollTimes = arr.getInteger(R.styleable.ScrollText_times, defScrollTimes);

        needScrollTimes = defScrollTimes;
        paint.setColor(textColor);
        paint.setTextSize(textSize);

        setZOrderOnTop(true);  //Control whether the surface view's surface is placed on top of its window.
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        DisplayMetrics metric = new DisplayMetrics();
        //activity获取DisplayMetrics
        //((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        //service获取DisplayMetrics
        metric = getResources().getDisplayMetrics();
        density = metric.density;

        //setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mHeight = getFontHeight(textSize);  //实际的视图高
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        // when layout width or height is wrap_content ,should init ScrollTextView Width/Height
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, viewHeight);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(viewWidth, mHeight);
            viewHeight = mHeight;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d(TAG, "surfaceHolder:" + surfaceHolder.toString() + "  arg1:" + i + "  arg2:" + i1 + "  arg3:" + i2);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bStop = false;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTextThread(), 10, scheduleTime, TimeUnit.MILLISECONDS);
        Log.d(TAG, "ScrollTextTextView is created");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        bStop = true;
        scheduledExecutorService.shutdownNow();
        Log.d(TAG, "ScrollTextTextView is destroyed");
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public void setTimes(int times) {
        if (times <= 0) {
            this.defScrollTimes = Integer.MAX_VALUE;
        } else {
            this.defScrollTimes = times;
            needScrollTimes = times;
        }
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public void setText(String text) {
        bStop = false;
        this.text = text;

        int length = text.length();
        Log.d("realmo", "字符串长度:" + length);
        if (length > 30) {
            //scheduledExecutorService.shutdownNow();
            this.scheduleTime = this.scheduleTime + (length - 30);
//            if (scheduledExecutorService!=null){
//                //bStop = true;
//                scheduledExecutorService.shutdown();
//                scheduledExecutorService=null;
//                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//                scheduledExecutorService.scheduleAtFixedRate(new ScrollTextThread(), 10, this.scheduleTime, TimeUnit.MILLISECONDS);
//            }else{
//                scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//                scheduledExecutorService.scheduleAtFixedRate(new ScrollTextThread(), 10, this.scheduleTime, TimeUnit.MILLISECONDS);
//            }
        }
    }

    //0到10
    public void setSpeed(int speed) {
        if (speed > 10 || speed < 0) {
            throw new IllegalArgumentException("Speed was invalid integer, it must between 0 and 10");
        } else {
            this.speed = speed;
        }
    }

    public void setTextColor(String colorString) {
        if (!TextUtils.isEmpty(colorString)){
            int color = Color.parseColor(colorString);
            this.textColor = color;
            paint.setColor(color);
        }
    }

    public void setTextSize(float size) {
        this.textSize = size;
        paint.setTextSize(size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("realmo", "触摸了滚动");
//        if (!clickEnable) {
//            return true;
//        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                bStop = !bStop;
//                if (!bStop && needScrollTimes == 0) {
//                    needScrollTimes = defScrollTimes;
//                }
//                break;
//        }
//        return true;
        return false;
    }

    /**
     * Draw text
     *
     * @param X X
     * @param Y Y
     */
    public synchronized void draw(float X, float Y) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        canvas.drawText(text, X, Y, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    /**
     * scroll text vertical
     */
    private void drawVerticalScroll() {
        List<String> strings = new ArrayList<>();
        int start = 0, end = 0;
        while (end < text.length()) {
            while (paint.measureText(text.substring(start, end)) < viewWidth && end < text.length()) {
                end++;
            }
            if (end == text.length()) {
                strings.add(text.substring(start, end));
                break;
            } else {
                end--;
                strings.add(text.substring(start, end));
                start = end;
            }
        }

        float fontHeight = getFontHeight(textSize / density);
        int GPoint = ((int) fontHeight + viewHeight) / 2;

        for (int n = 0; n < strings.size(); n++) {
            if (bStop) {
                return;
            }
//            Log.e(TAG, Thread.currentThread().getName() + "  Drawing:   " + strings.get(n) + "\n");
            for (float i = viewHeight + fontHeight; i > -fontHeight; i = i - 3) {
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
                canvas.drawText(strings.get(n), 0, i, paint);
                surfaceHolder.unlockCanvasAndPost(canvas);
                if (i - GPoint < 4 && i - GPoint > 0) {
                    if (bStop) {
                        return;
                    }
                    try {
                        Thread.sleep(speed * 1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        }
    }

    class ScrollTextThread implements Runnable {
        @Override
        public void run() {
            textWidth = paint.measureText(text);
            viewWidth_plus_textLength = viewWidth + textWidth;
            textY = (viewHeight + getFontHeight(textSize / density)) / 2 + getPaddingTop() - getPaddingTop();
            textX = viewWidth - viewWidth / 5;

            while (!bStop) {
                //如果文字太短不需要滚动
//                if (textWidth < getWidth()) {
//                    draw(1, textY);
//                    bStop = true;
//                    break;
//                }
                if (isHorizontal) {
                    draw(viewWidth - textX, textY);
                    textX += speed;
                    if (textX > viewWidth_plus_textLength) {
                        textX = 0;
                        --needScrollTimes;
                    }
                } else {
                    drawVerticalScroll();
                    --needScrollTimes;
                }
                if (needScrollTimes <= 0) {
                    bStop = true;
                }
            }
        }
    }

}