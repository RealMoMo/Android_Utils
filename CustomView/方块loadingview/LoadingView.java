package com.newline.ucos.launcher.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import androidx.annotation.Nullable;

import com.newline.ucos.launcher.R;


public class LoadingView extends View {

    // 半个方块的宽度
    private float mBlockWidthHalf;
    // 方块之间的间隔
    private float mBlockMargin;
    // 控件中唯一一个移动方块
    private MoveBlock mMoveBlock;
    // 移动方块的初始位置
    private int mStartPosition;
    // 当前移动方块的位置
    private int mCurrentPosition;
    // 移动方块的圆角半径
    private float mMoveBlockAngle;
    // 固定方块数组
    private FixedBlock[] mFixedBlocks;
    // 固定方块的圆角半径
    private float mFixBlockAngle;
    // 行数及列数
    private int mLines;
    // 方块的颜色
    private int mBlockColor;
    // 移动方块是否顺时针转动
    private boolean mClockwise;
    // 动画持续时间
    private int mDuration;
    // 旋转动画的当前旋转角度
    private float mCurrentDegree;
    // 动画插值器资源id
    private int mInterpolatorId;
    // 动画插值器
    private Interpolator mInterpolator;

    private boolean isRunning;

    private Paint mPaint;
    private Paint mPaint1;
    private boolean isFirst = true;
    private boolean isPrepare = false;

    /**
     * java代码中创建时调用
     *
     * @param context
     */
    public LoadingView(Context context) {
        this(context, null);
    }

    /**
     * xml代码中创建会调用
     *
     * @param context
     * @param attrs
     */
    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        intiAttrs(context, attrs);

        initPaint();

        initBlocks();
    }

    /**
     * 开启动画
     */
    public void start() {
        isRunning = true;
        // 获取当前动画移动方块起始处
        FixedBlock AnimFrom = mFixedBlocks[mCurrentPosition].previous;
        // 获取当前动画移动方块终点处
        FixedBlock AnimTo = mFixedBlocks[mCurrentPosition];

        // 组合动画 = 平移 + 旋转
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(mInterpolator);
        animatorSet.playTogether(createTranslateValueAnimator(AnimFrom, AnimTo), createRotateValueAnimator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // 每次动画开始时更新移动方块的位置
                updateMoveBlock();
                // 隐藏移动方块位置的固定方块
                mFixedBlocks[mCurrentPosition].previous.isShow = false;
                // 显示移动方块
                mMoveBlock.isShow = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFirst) {
                    isFirst = false;
                    initFixedBlockPosition();
                }
                // 显示移动方块位置的固定方块
                mFixedBlocks[mCurrentPosition].isShow = true;
                mCurrentPosition = mFixedBlocks[mCurrentPosition].previous.index;
                // 隐藏移动方块
                mMoveBlock.isShow = false;
                // 循环动画
                if(isRunning){
                    start();
                }

            }
        });

        // 启动动画
        animatorSet.start();
    }


    public void stop(){
        isRunning = false;
    }

    /**
     * 控件尺寸发生改变时会调用，因此初始化控件的时候会首先调用一次
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        initFixedBlockPosition();

        initMoveBlockPosition();

        start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制固定方块
        for (FixedBlock mFixedBlock : mFixedBlocks) {
            if (mFixedBlock.isShow) {
                canvas.drawRoundRect(mFixedBlock.rectF, mFixBlockAngle, mFixBlockAngle, mPaint);
            }
        }
        // 绘制移动方块
        if (mMoveBlock.isShow) {
            canvas.rotate(mClockwise ? mCurrentDegree : -mCurrentDegree, mMoveBlock.cx, mMoveBlock.cy);
            canvas.drawRoundRect(mMoveBlock.rectF, mMoveBlockAngle, mMoveBlockAngle, mPaint);
        }
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    private void intiAttrs(Context context, @Nullable AttributeSet attrs) {
        // 加载自定义属性集合
        // 尺寸属性缺省值应该在dimens.xml文件中定义，不能只是传递一个数值。否则会导致在不同分辨率的手机上显示大小不一致
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        // 实现动画效果，lines值不得少于2；
        mLines = typedArray.getInteger(R.styleable.LoadingView_lines, 3);
        mLines = mLines < 2 ? 2 : mLines;
        // 如果设置的值是Xdp | Xsp，则返回X * density; 如果设置的是Xpx，则返回X；
        mBlockWidthHalf = typedArray.getDimension(R.styleable.LoadingView_layout_blockWidth_half, 30f);
        mBlockMargin = typedArray.getDimension(R.styleable.LoadingView_layout_blockMargin, 10f);
        mStartPosition = typedArray.getInteger(R.styleable.LoadingView_layout_startPosition, 0);
        // 移动方块不是外部方块则默认其起始位置为0
        if (!isOuterBlock(mStartPosition, mLines)) {
            mStartPosition = 0;
        }
        mCurrentPosition = mStartPosition;

        mMoveBlockAngle = typedArray.getFloat(R.styleable.LoadingView_moveBlock_angle, 10f);
        mFixBlockAngle = typedArray.getFloat(R.styleable.LoadingView_fixBlock_angle, 30f);
        mBlockColor = typedArray.getColor(R.styleable.LoadingView_blockColor, Color.MAGENTA);
        mClockwise = typedArray.getBoolean(R.styleable.LoadingView_isClockwise, true);
        mDuration = typedArray.getInteger(R.styleable.LoadingView_duration, 250);
        mInterpolatorId = typedArray.getResourceId(R.styleable.LoadingView_interpolator, android.R.anim.bounce_interpolator);
        mInterpolator = AnimationUtils.loadInterpolator(context, mInterpolatorId);

        typedArray.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        // 创建抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mBlockColor);

        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(Color.BLUE);

    }

    /**
     * 初始化固定方块、移动方块
     * 外部方块之间一一关联
     */
    private void initBlocks() {
        // 确定行数、列数为mLines时的固定方块总数
        mFixedBlocks = new FixedBlock[mLines * mLines];
        // 创建所有固定方块并保存在数组中
        for (int i = 0; i < mFixedBlocks.length; i++) {
            FixedBlock fixedBlock = new FixedBlock();
            fixedBlock.index = i;
            // 如果固定方块的index为移动方块的mStartPosition时，isShow为false
            fixedBlock.isShow = mStartPosition != i;
            fixedBlock.rectF = new RectF();
            mFixedBlocks[i] = fixedBlock;
        }

        // 创建一个移动方块
        mMoveBlock = new MoveBlock();
        mMoveBlock.rectF = new RectF();
        mMoveBlock.isShow = false;

        contactOuterBlocks();
    }

    /**
     * 使外部的方块之间一一关联
     * 根据mClockwise有两种不同的关联关系
     */
    private void contactOuterBlocks() {
        // 先默认为方块转动为顺时针，mClockwise == true;
        for (int i = 0; i < mFixedBlocks.length; i++) {

            if (i < mLines) {
                // 进行第一行的关联
                if (i % mLines == 0) {
                    // 左上角方块
                    mFixedBlocks[i].next = mFixedBlocks[i + 1];
                    mFixedBlocks[i].previous = mFixedBlocks[i + mLines];
                } else if (i % mLines == mLines - 1) {
                    // 右上角方块
                    mFixedBlocks[i].next = mFixedBlocks[i + mLines];
                    mFixedBlocks[i].previous = mFixedBlocks[i - 1];
                } else {
                    mFixedBlocks[i].next = mFixedBlocks[i + 1];
                    mFixedBlocks[i].previous = mFixedBlocks[i - 1];
                }

            } else if (i % mLines == mLines - 1) {
                // 进行最后一列的关联
                if (i / mLines == 0) {
                    // 右上角方块关系已确立，无需任何操作
                } else if (i == mFixedBlocks.length - 1) {
                    // 右下角方块
                    mFixedBlocks[i].next = mFixedBlocks[i - 1];
                    mFixedBlocks[i].previous = mFixedBlocks[i - mLines];
                } else {
                    mFixedBlocks[i].next = mFixedBlocks[i + mLines];
                    mFixedBlocks[i].previous = mFixedBlocks[i - mLines];
                }

            } else if (i / mLines == mLines - 1) {
                // 进行最后一行的关联
                if (i == mFixedBlocks.length - 1) {
                    // 右下角方块关系已确立，无需任何操作
                } else if (i % mLines == 0) {
                    // 左下角方块
                    mFixedBlocks[i].next = mFixedBlocks[i - mLines];
                    mFixedBlocks[i].previous = mFixedBlocks[i + 1];
                } else {
                    mFixedBlocks[i].next = mFixedBlocks[i - 1];
                    mFixedBlocks[i].previous = mFixedBlocks[i + 1];
                }

            } else if (i % mLines == 0) {
                // 进行第一列的关联
                if (i / mLines == 0) {
                    // 左上角方块关系已确立，无需任何操作
                } else if (i % mLines == mLines - 1) {
                    // 左下角反方块关系已确立，无需任何操作
                } else {
                    mFixedBlocks[i].next = mFixedBlocks[i - mLines];
                    mFixedBlocks[i].previous = mFixedBlocks[i + mLines];
                }
            }
        }

        // 判断旋转方向
        if (mClockwise) {
            // 如果是顺时针，无需任何操作
        } else {
            FixedBlock temp;
            for (FixedBlock f : mFixedBlocks) {
                temp = f.next;
                f.next = f.previous;
                f.previous = temp;
            }
        }
    }

    /**
     * 移动方块只能在外围移动，需要判断是否为外围的方块
     */
    private boolean isOuterBlock(int position, int lines) {
        // 匹配第一行；匹配最后一行；匹配第一列；匹配最后一列
        return position < lines
                || position >= lines * (lines - 1)
                || position % lines == 0
                || position % lines == 3;
    }

    /**
     * 确立固定方块的位置
     */
    private void initFixedBlockPosition() {
        // 左上角index为0的方块RectF坐标
        float left;
        float top;
        float right;
        float bottom;
        // 根据左上角index为0的方块确立的新的方块坐标
        float newLeft;
        float newTop;
        float newRight;
        float newBottom;
        // 方块的宽高
        float blockWidth = mBlockWidthHalf * 2f;
        // LoadingView中心坐标
        int cx = getMeasuredWidth() / 2;
        int cy = getMeasuredHeight() / 2;

        // 确立左上角方块
        left = cx - mLines / 2f * blockWidth - (mLines - 1) / 2f * mBlockMargin;
        top = cy - mLines / 2f * blockWidth - (mLines - 1) / 2f * mBlockMargin;
        right = left + blockWidth;
        bottom = top + blockWidth;

        // 第一行
        for (int i = 0; i < mLines; i++) {
            // 第一列
            for (int j = 0; j < mLines; j++) {
                newLeft = left + (blockWidth + mBlockMargin) * j;
                newTop = top + (blockWidth + mBlockMargin) * i;
                newRight = newLeft + blockWidth;
                newBottom = newTop + blockWidth;
                mFixedBlocks[i * mLines + j].rectF = new RectF(newLeft, newTop, newRight, newBottom);
            }
        }
    }

    /**
     * 确立移动方块的位置
     */
    private void initMoveBlockPosition() {
        mMoveBlock.rectF = mFixedBlocks[mStartPosition].previous.rectF;
    }

    /**
     * 设置平移动画,平移的对象为移动方块mMoveBlock
     *
     * @param from 平移的起始，即isShow为false的外围方块位置
     * @param to   平移的终点，from方块的next指向的下一个方块位置
     */
    private ValueAnimator createTranslateValueAnimator(FixedBlock from, FixedBlock to) {
        float startAnimValue = 0;
        float endAnimValue = 0;
        PropertyValuesHolder left = null;
        PropertyValuesHolder top = null;

        // 设置动画时长
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(mDuration);
        // 设置移动方向
        if (mClockwise) {
            // 顺时针时
            if (from.index < mLines && to.index < mLines || from.index >= mLines * (mLines - 1) && to.index >= mLines * (mLines - 1)) {
                // 行移动时
                startAnimValue = from.rectF.left;
                if (from.index < to.index) {
                    // 在第一行移动
                    endAnimValue = startAnimValue + mBlockMargin;
                } else {
                    // 在最后一行移动
                    endAnimValue = startAnimValue - mBlockMargin;
                }
                // 设置属性值
                left = PropertyValuesHolder.ofFloat("left", startAnimValue, endAnimValue);
                valueAnimator.setValues(left);

            } else {
                // 列移动时
                startAnimValue = from.rectF.top;
                if (from.index > to.index) {
                    // 在第一列移动
                    endAnimValue = startAnimValue - mBlockMargin;
                } else {
                    // 在最后一列移动
                    endAnimValue = startAnimValue + mBlockMargin;
                }
                // 设置属性值
                top = PropertyValuesHolder.ofFloat("top", startAnimValue, endAnimValue);
                valueAnimator.setValues(top);
            }

        } else {
            // 逆时针时
            if (from.index < mLines && to.index < mLines || from.index >= mLines * (mLines - 1) && to.index >= mLines * (mLines - 1)) {
                // 行移动时
                startAnimValue = from.rectF.left;
                if (from.index > to.index) {
                    // 在第一行移动
                    endAnimValue = startAnimValue - mBlockMargin;
                } else {
                    // 在最后一行移动
                    endAnimValue = startAnimValue + mBlockMargin;
                }
                // 设置属性值
                left = PropertyValuesHolder.ofFloat("left", startAnimValue, endAnimValue);
                valueAnimator.setValues(left);

            } else {
                // 列移动时
                startAnimValue = from.rectF.top;
                if (from.index < to.index) {
                    // 在第一列移动
                    endAnimValue = startAnimValue + mBlockMargin;
                } else {
                    // 在最后一列移动
                    endAnimValue = startAnimValue - mBlockMargin;
                }
                // 设置属性值
                top = PropertyValuesHolder.ofFloat("top", startAnimValue, endAnimValue);
                valueAnimator.setValues(top);
            }
        }

        // 添加监听器更新属性值
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float left = (Float) animation.getAnimatedValue("left");
                Float top = (Float) animation.getAnimatedValue("top");
                if (left != null) {
                    mMoveBlock.rectF.offsetTo(left, mMoveBlock.rectF.top);
                }
                if (top != null) {
                    mMoveBlock.rectF.offsetTo(mMoveBlock.rectF.left, top);
                }

                updateRotateCenter();
                // 更新绘制，调用onDraw()
                invalidate();
            }
        });
        return valueAnimator;
    }

    /**
     * 设置旋转动画
     */
    private ValueAnimator createRotateValueAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 90f);
        valueAnimator.setDuration(mDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object animationValue = animation.getAnimatedValue();
                mCurrentDegree = (float) animationValue;
                // 重新绘制，调用onDraw()
                invalidate();
            }
        });
        return valueAnimator;
    }

    /**
     * 动画过程中实时更新移动方块mMoveBlock旋转动画的旋转中心。
     * 平移动画+旋转动画的组合动画，其中的旋转动画的旋转中心会因平移动画而发生改变，分为四个旋转中心
     */
    private void updateRotateCenter() {
        int index = mMoveBlock.index;
        float left = mMoveBlock.rectF.left;
        float top = mMoveBlock.rectF.top;
        float right = mMoveBlock.rectF.right;
        float bottom = mMoveBlock.rectF.bottom;

        // 视图左上角位置时
        if (index == 0) {
            // 移动方块的右下角为旋转中心
            mMoveBlock.cx = right;
            mMoveBlock.cy = bottom;
        }
        // 视图右上角位置时
        else if (index == mLines - 1) {
            // 移动方块的左下角为旋转中心
            mMoveBlock.cx = left;
            mMoveBlock.cy = bottom;
        }
        // 视图左下角位置时
        else if (index == mLines * (mLines - 1)) {
            // 移动方块的右上角为旋转中心
            mMoveBlock.cx = right;
            mMoveBlock.cy = top;
        }
        // 视图右下角位置时
        else if (index == mLines * mLines - 1) {
            // 移动方块的右下角为旋转中心
            mMoveBlock.cx = left;
            mMoveBlock.cy = top;


        } else if (index % mLines == 0) {
            // 第一列时
            mMoveBlock.cx = right;
            mMoveBlock.cy = mClockwise ? top : bottom;
        } else if (index < mLines) {
            // 第一行时
            mMoveBlock.cx = mClockwise ? right : left;
            mMoveBlock.cy = bottom;
        } else if (index % mLines == mLines - 1) {
            // 最后一列时
            mMoveBlock.cx = left;
            mMoveBlock.cy = mClockwise ? bottom : top;
        } else if (index / mLines == mLines - 1) {
            // 最后一行时
            mMoveBlock.cx = mClockwise ? left : right;
            mMoveBlock.cy = top;
        }

    }

    /**
     * 更新移动方块的参数和旋转中心
     */
    private void updateMoveBlock() {
        // 坐标
        mMoveBlock.rectF.set(mFixedBlocks[mCurrentPosition].previous.rectF);
        // 索引
        mMoveBlock.index = mFixedBlocks[mCurrentPosition].previous.index;
        // 旋转中心
        updateRotateCenter();
    }

    /**
     * 固定方块所属类
     */
    private class FixedBlock {

        // 储存坐标位置参数
        RectF rectF;
        // 方块对应序号
        int index;
        // 刷新时是否需要绘制的标志位
        boolean isShow;
        // 当自身为外围的方块时，指向的下一个外围方块
        FixedBlock next;
        // 当自身为外围的方块时，指向自身的上一个外围方块
        FixedBlock previous;
    }

    /**
     * 移动方块所属类
     */
    private class MoveBlock {
        // 储存坐标位置参数
        RectF rectF;
        // 方块对应序号
        int index;
        // 刷新时是否需要绘制的标志位
        boolean isShow;
        // 旋转时的旋转中心坐标(x, y)
        float cx;
        float cy;
    }
}
