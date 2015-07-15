package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.qingfengmy.R;

/**
 * Created by Administrator on 2015/7/15.
 */
public class LuckPanSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    // 用于绘制的线程
    private Thread t;
    // 线程的控制开关
    private boolean isRunning;

    // 奖项
    private String[] mStrs = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE", "服装一套", "恭喜发财"};
    // 奖品图片
    private int[] mImgs = new int[]{R.drawable.danfan, R.drawable.ipad, R.drawable.f040, R.drawable.iphone, R.drawable.meizi, R.drawable.f040};
    // 与图片对应的bitmap数组
    private Bitmap[] mImgsBitmap;
    // 盘块背景色
    private int[] mColor = new int[]{0xffffc300, 0xfff17e01, 0xffffc300, 0xfff17e01, 0xffffc300, 0xfff17e01};

    // 整个盘的范围
    private RectF mRange = new RectF();
    // 盘的直径
    private int mRadius;
    // 盘的画笔
    private Paint mArcPaint;
    // 文本的画笔
    private Paint mTextPaint;

    // 盘滚动的速度
    private double mSpeed;
    // 滚动的起始角度（可能多线程操作）
    private volatile float mStartAngle;

    // 是否点击了停止按钮（避免多次点击）
    private boolean isShouldEnd;
    // 中心位置
    private int mCenter;
    // padding以paddingLeft为准
    private int mPadding;
    // 背景图
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
    // 文字大小
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    int mItemCount;

    public LuckPanSurface(Context context) {
        super(context);
    }

    public LuckPanSurface(Context context, AttributeSet attrs) {
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

        mPadding = getPaddingLeft();

        mRadius = width - mPadding * 2;

        mCenter = width / 2;

        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 初始化 画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true); // 抗锯齿
        mArcPaint.setDither(true); // 防抖动

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xffffffff);

        // 绘制范围
        mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);


        mItemCount = mImgs.length;
        // 图片
        mImgsBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }
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
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                // 每次刷新不小于50毫秒
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {
        // 按home键回到主界面后，系统可能回收一些东西，导致子线程出错，这里try-catch一下。
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                // 绘制背景
                drawBg();
                // 绘制盘块
                float tempAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;

                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColor[i]);
                    /**
                     * 绘制一段弧线
                     * oval :指定圆弧的外轮廓矩形区域。
                     startAngle: 圆弧起始角度，单位为度。
                     sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度。
                     useCenter: 如果为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
                     paint: 绘制圆弧的画板属性，如颜色，是否填充等。
                     */
                    mCanvas.drawArc(mRange, tempAngle, sweepAngle, true, mArcPaint);

                    // 绘制文本
                    drawText(tempAngle, sweepAngle, mStrs[i]);

                    // 绘制图片
                    drawIcon(tempAngle, mImgsBitmap[i]);
                    tempAngle += sweepAngle;
                }

                mStartAngle += mSpeed;

                // 点击了停止按钮，缓慢的停下
                if (isShouldEnd) {
                    mSpeed -= 1;
                }

                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
            }
        } catch (Exception e) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * 点击启动旋转
     */
    public void luckyStart(int index) {
        // 计算每一项的角度
        float angle = 360 / mItemCount;

        // 计算每一项的中奖范围（当前index）
        // 0-> 210-270
        // 1-> 150-210
        float from = 270 - (index + 1) * angle;
        float end = from + angle;

        // 设置停下来的距离
        float targetFrom = 4 * 360 + from;
        float targetEnd = 4 * 360 + end;
        /**
         * <pre>
         *  等差数列
         *  100+99+98+...= speed
         *  (v1 + 0) * (v1+1) / 2 = target ;
         *  v1*v1 + v1 - 2target = 0 ;
         *  v1=-1+(1*1 + 8 *1 * target)/2;
         * </pre>
         */
        float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
        float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetEnd) - 1) / 2;
        mSpeed = (float) (v1 + Math.random() * (v2 - v1));
        isShouldEnd = false;
    }

    /**
     * 点击停止
     */
    public void luckyEnd() {
        mStartAngle = 0;
        isShouldEnd = true;
    }

    /**
     * 是否在旋转
     *
     * @return
     */
    public boolean isStart() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }

    /**
     * 绘制图片
     *
     * @param tempAngle
     * @param bitmap
     */
    private void drawIcon(float tempAngle, Bitmap bitmap) {
        // 设置图片的宽度
        int imgWidth = mRadius / 8;

        // Math.PI/180
        float angle = (float) ((tempAngle + 30) * (Math.PI / 180));

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        // 确定图片位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    /**
     * 绘制盘块的文本
     *
     * @param tempAngle
     * @param sweepAngle
     * @param mStr
     */
    private void drawText(float tempAngle, float sweepAngle, String mStr) {
        Path path = new Path();
        path.addArc(mRange, tempAngle, sweepAngle);
        // 利用水平偏移量让文字居中
        int textWith = (int) mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius * Math.PI / mItemCount / 2 - textWith / 2);
        int vOffset = mRadius / 10;
        /**
         * 水平偏移量和垂直偏移量hoffset和vOffset
         */
        mCanvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        mCanvas.drawColor(0xffffffff);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), null);
    }
}
