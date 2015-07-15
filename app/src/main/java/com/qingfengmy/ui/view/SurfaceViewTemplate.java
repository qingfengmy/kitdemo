package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2015/7/15.
 */
public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    // 用于绘制的线程
    private Thread t;
    // 线程的控制开关
    private boolean isRunning;

    public SurfaceViewTemplate(Context context) {
        super(context);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHolder = getHolder();

        mHolder.addCallback(this);

        // 可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        // 设置长亮
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {
        // 不断进行绘制
        while (isRunning) {
            draw();
        }
    }

    private void draw() {
        // 按home键回到主界面后，系统可能回收一些东西，导致子线程出错，这里try-catch一下。
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                // draw something

            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}
