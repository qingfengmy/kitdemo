package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * User: zhangtao
 * Date: 2014-12-25
 * Time: 17:59
 */
public class GuaGuaKa extends TextView {

    // 上面的画笔
    Paint mOutterPaint;
    // 记录手指划过的路径
    Path mPath;

    Canvas mCanvas;
    Bitmap mBitmap;


    public GuaGuaKa(Context context, AttributeSet attrs) {
        super(context, attrs);

        mOutterPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 上面的方法执行完后，我们可以拿到测量出的宽高
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // 初始化bitmap的大小
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 实例化canvas
        mCanvas = new Canvas(mBitmap);

        // 设置画笔属性
        setOutterPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 先画灰白底
        mOutterPaint.setColor(0xc0c0c0);
        mCanvas.drawBitmap(mBitmap, 0, 0, mOutterPaint);
        // 再画path
        drawPath();
    }


    int mLastX, mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3)
                {
                    mPath.lineTo(x, y);
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void drawPath() {
        // 画path时设置为空心
        mOutterPaint.setStyle(Paint.Style.STROKE);
        // 设置过度时的mode
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    // 设置outter画笔属性
    private void setOutterPaint() {
        // 抗锯齿(antialias-抗锯齿)
        mOutterPaint.setAntiAlias(true);
        // 仿抖动(dither-抖动)
        mOutterPaint.setDither(true);
        //画笔接洽点类型 如影响矩形但角的外轮廓(stroke-笔锋，join-结合)
        mOutterPaint.setStrokeJoin(Paint.Join.ROUND);
        //画笔笔刷类型 如影响画笔但始末端(stroke--笔锋，cap-帽子，盖子)
        mOutterPaint.setStrokeCap(Paint.Cap.ROUND);
        //画笔类型 STROKE空心 FILL 实心 FILL_AND_STROKE 用契形填充
        mOutterPaint.setStyle(Paint.Style.FILL);
        mOutterPaint.setStrokeWidth(20);
    }
}
