package com.qingfengmy.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * valueAnimator动画，这里只是提供了变化梯度，然后在各个梯度变化时，重新执行draw方法。
 * view不执行动画，而是通过draw方法，不断改变x或y值来完成动画效果。
 */
public class SouGouLoadingView extends View {
    private final String TAG = "aaa";
    //画笔
    private Paint mPaint;
    //颜色
    private int color = Color.parseColor("#0000FF");
    //半径
    private int radius = 10;
    private float density;
    private RectF rectF;
    //起点、终点、当前点
    private int startY, startX, endY, currentY;

    public SouGouLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = getResources().getDisplayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    boolean isFirst = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isFirst) {
            //获取视图的中心点
            startX = getMeasuredWidth() / 2;
            endY = getMeasuredHeight() / 2;
            startY = endY * 5 / 6;
            isFirst =false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(color);
        if (currentY == 0) {
            playAnimator();
        } else {
            drawCircle(canvas);
            drawShader(canvas);
        }

        Log.e("aaa", "endY=" + endY + ",currentY=" + currentY);
    }

    //动画执行
    private void playAnimator() {
        //我们只需要取Y轴方向上的变化即可
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startY, endY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentY = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator(1.2f));
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(2);
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    /**
     * 绘制圆形
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        //当接触到底部时候，我们为了要描绘一种压扁的效果
        if (endY - currentY > 10) {
            canvas.drawCircle(startX, currentY, radius * density, mPaint);
        } else {
            rectF = new RectF(startX - radius * density - 2, currentY - radius * density + 5,
                    startX + radius * density + 2, currentY + radius * density);
            canvas.drawOval(rectF, mPaint);
            Log.d(TAG, "Oval");
        }
    }

    /**
     * 绘制阴影部分，由椭圆来支持，根据高度比来底部阴影的大小
     */
    private void drawShader(Canvas canvas) {
        //计算差值高度
        int dx = endY - startY;
        //计算当前点的高度差值
        int dx1 = currentY - startY;
        float ratio = (float) (dx1 * 1.0 / dx);
        if (ratio <= 0.3) {//当高度比例小于0.3，所在比较高的时候就不进行绘制影子
            return;
        }
        int ovalRadius = (int) (radius * ratio * density);
        //设置倒影的颜色
        mPaint.setColor(Color.parseColor("#3F3B2D"));
        //绘制椭圆
        rectF = new RectF(startX - ovalRadius, endY + 10, startX + ovalRadius, endY + 15);
        canvas.drawOval(rectF, mPaint);
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
    }
}
