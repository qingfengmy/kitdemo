package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/5/18.
 */
public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener
        , ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mOnce;
    // 初始化时缩放的值
    private float mInitScale;

    private float mMinScale;

    private float mMaxScale;

    private Matrix mScaleMatrix;

    // 系统封装的手势缩放工具类
    // 捕获用户多指触控时缩放比例
    private ScaleGestureDetector mScaleGestureDetector;


    // 上次多点触控数量
    private int mLastPointCount;

    private float mLastX, mLastY;

    // 取系统的一个边界值
    private int mTouchSlop;
    private boolean isCanDrag;

    private boolean isCheckLeftAndRight, isCheckTopAndBottom;
    // 系统封装的手势识别工具
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);
        // 取系统定义好的边界值
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale == true)
                    return true;

                float x = e.getX();
                float y = e.getY();

                if (getScale() < mMinScale)
                {
                    ZoomImageView.this.postDelayed(
                            new AutoScaleRunnable(mMinScale, x, y), 16);
                    isAutoScale = true;
                } else if (getScale() >= mMinScale
                        && getScale() < mMaxScale)
                {
                    ZoomImageView.this.postDelayed(
                            new AutoScaleRunnable(mMaxScale, x, y), 16);
                    isAutoScale = true;
                } else
                {
                    ZoomImageView.this.postDelayed(
                            new AutoScaleRunnable(mInitScale, x, y), 16);
                    isAutoScale = true;
                }

                return super.onDoubleTap(e);
            }
        });
    }


    /**
     * 自动缩放
     */
    private class AutoScaleRunnable implements Runnable {

        // 缩放的目标值
        private float mTargetScale;
        private float x, y;

        private final float BIGGER = 1.07f;
        private final float SMALLER = 0.93f;

        private float tempScale;

        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tempScale = BIGGER;
            } else if (getScale() > mTargetScale) {
                tempScale = SMALLER;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tempScale, tempScale, x, y);
            checkBorderWhenScale();
            setImageMatrix(mScaleMatrix);

            float currentScale = getScale();
            if ((tempScale > 1.0f && currentScale < mTargetScale) || (tempScale < 1.0f && currentScale > mTargetScale)) {
                postDelayed(this, 16);
            }else{
                // 设置为目标值
                float scale = mTargetScale/currentScale;
                mScaleMatrix.postScale(scale, scale, x, y);
                checkBorderWhenScale();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }
        }
    }


    // 缩放区间 minScale-maxScale
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 当前图片缩放值
        float scale = getScale();

        // 手势操作后的缩放值
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        // 缩放范围的控制
        // 不到最大，还要继续放大；如果不到最小，还行缩小；都允许
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {

            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }

            // 以触摸点为中心缩放
            mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());

            checkBorderWhenScale();

            setImageMatrix(mScaleMatrix);
        }

        return true;
    }

    /**
     * 在缩放时，控制边界和中心
     */
    private void checkBorderWhenScale() {

        RectF rectF = getMatrixRectF();
        // 缩放后的边界偏移值
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        if (rectF.width() >= width) {
            // 如果图片宽度比屏幕宽度大，才处理空白问题；如果比屏幕宽度小，有空白也正常
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }

        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;
            }
        }

        if (rectF.width() < width) {
            // 如果宽高小于屏幕宽高，则居中
            deltaX = width / 2f - rectF.right + rectF.width() / 2f;
        }

        if (rectF.height() < height) {
            // 如果宽高小于屏幕宽高，则居中
            deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 获取放大和缩小后的图片宽高和边界值
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // 返回true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;

        // 手指数量
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        // 中心点
        x /= pointerCount;
        y /= pointerCount;

        if (mLastPointCount != pointerCount) {
            // 手指数量发生改变
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointCount = pointerCount;

        RectF rectF = getMatrixRectF();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth()+0.01 || rectF.height() > getHeight()+0.01)
                {   // 图片宽高大于控件宽高时，移动是移动图片
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    // 图片移动
                    if (getDrawable() != null) {
                        if (getMatrixRectF().left == 0 && dx > 0)
                        {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }

                        if (getMatrixRectF().right == getWidth() && dx < 0)
                        {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 图片宽度小于控件宽度，不允许移动
                        if (rectF.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        // 图片宽度小于控件宽度，不允许移动
                        if (rectF.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mScaleMatrix);
                    }
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointCount = 0;
                break;
        }
        return true;
    }



    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    private float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 绑定接口
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 移除接口
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        // 全局的布局完成以后回调的方法
        // 获取加载的图片
        if (!mOnce) {
            // 得到控件的宽和高
            int width = getWidth();
            int height = getHeight();
            // 得到图片的宽和高
            Drawable drawable = getDrawable();
            if (drawable == null)
                return;
            int dw = drawable.getIntrinsicWidth();
            int dh = drawable.getIntrinsicHeight();

            float scale = 1.0f;
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height)
            {
                scale = width * 1.0f / dw;
            }
            if (dh > height && dw <= width)
            {
                scale = height * 1.0f / dh;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height)
            {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            mInitScale = scale;
            mMinScale = mInitScale * 2f;
            mMaxScale = mInitScale * 4f;
            // 将图片移动至控件的中心
            int dx = width / 2 - dw / 2;
            int dy = height / 2 - dh / 2;

            // 平移
            mScaleMatrix.postTranslate(dx, dy);
            // 缩放
            // 中心点
            mScaleMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mScaleMatrix);

            mOnce = true;
        }
    }

    // 移动时进行边界检查
    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            // 上面有白边
            deltaY = -rectF.top;
        }

        if (rectF.bottom < height && isCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }
        if (rectF.left > 0 && isCheckLeftAndRight) {
            deltaX = -rectF.left;
        }

        if (rectF.right < width && isCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }

        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) > mTouchSlop;
    }
}
