package com.qingfengmy.ui.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2015/10/29.
 */
public class BaiDuLoadingView extends View {

    private Paint paint;
    private boolean hasInit;
    private float density;
    // 中间位置
    private int x, y;
    // 两边圆运动距离
    private int end;

    private int radius;

    private float currentX;
    ValueAnimator animator;
    //指定的颜色
    private int colors[] = new int[]{Color.parseColor("#EE454A"), Color.parseColor("#2E9AF2"),
            Color.parseColor("#616161")};

    //颜色的下标
    private int colorIndex = 0;

    public BaiDuLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        density = getResources().getDisplayMetrics().density;
        radius = (int) (10 * density);
        end = (int) (60 * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!hasInit) {
            x = getMeasuredWidth() / 2;
            y = getMeasuredHeight() / 2;
            hasInit = true;
        }
    }

    private void initAnimator() {
        // 初始值不能是0
        animator = ValueAnimator.ofFloat(10, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentX = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(500);
        animator.setRepeatCount(-1);
//       1 RESTART：重新从头开始执行。
//       2 REVERSE：反方向执行。
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentX == 0) {
            initAnimator();
        } else {
            // currentX == end避免误差写成
            if (Math.abs(end - currentX) < 0.1f) {
                colorIndex++;
            }
            Log.e("aaa", "abs=" + Math.abs(end - currentX));
            drawMiddleCircle(canvas);
            drawLeftCircle(canvas);
            drawRightCircle(canvas);
        }
    }

    private void drawRightCircle(Canvas canvas) {
        paint.setColor(colors[colorIndex % 3]);
        int rightX = (int) (x + end - currentX);
        canvas.drawCircle(rightX, y, radius, paint);
    }

    private void drawLeftCircle(Canvas canvas) {
        paint.setColor(colors[(colorIndex + 1) % 3]);
        int leftX = (int) (x - end + currentX);
        canvas.drawCircle(leftX, y, radius, paint);
    }

    private void drawMiddleCircle(Canvas canvas) {
        paint.setColor(colors[(colorIndex + 2) % 3]);
        canvas.drawCircle(x, y, radius, paint);
    }
}
