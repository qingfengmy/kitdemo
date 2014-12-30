package com.qingfengmy.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingfengmy.R;

/**
 * User: zhangtao
 * Date: 2014-12-25
 * Time: 17:59
 */
public class GuaGuaKa extends View {

    // 前景的画笔
    Paint mOutterPaint;
    // 记录手指划过的路径
    Path mPath;
    // 前景图片
    private Bitmap mOutterBitmap;

    Canvas mCanvas;
    Bitmap mBitmap;
    // 手指移动的时候记录位置
    int mLastX, mLastY;

    // 背景的画笔
    Paint mBackPaint;

    // 文本信息
    private String mText;
    private Rect mTextBound;
    private int mTextSize;
    private int mTextColor;
    // 当前view的宽高
    int width;
    int height;

    public GuaGuaKa(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取自定义属性值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GuaGuaKa);
        try {
            mText = a.getString(R.styleable.GuaGuaKa_text);
            mTextColor = a.getColor(R.styleable.GuaGuaKa_textColor, 0);
            mTextSize = a.getDimensionPixelSize(R.styleable.GuaGuaKa_textSize, 0);
        } finally {
            if (a != null)
                a.recycle();
        }

        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mOutterPaint = new Paint();
        mPath = new Path();

        mOutterBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.fg_guaguaka);
//        mText = "谢谢惠顾";
        mTextBound = new Rect();
        mBackPaint = new Paint();
//        mTextColor = Color.parseColor("#ffffff");
//        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                22, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 上面的方法执行完后，我们可以拿到测量出的宽高
        width = getMeasuredWidth();
        height = getMeasuredHeight();

        // 初始化bitmap的大小
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 实例化canvas
        mCanvas = new Canvas(mBitmap);

        // 设置画笔属性
        setOutterPaint();
        setUpBackPaint();

        // 画灰白底/画背景图(放到这里只画一次)
        drawGrayBg();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画文字(文字是以左下角左边进行定位的)
        int textX = getWidth() / 2 - mTextBound.width() / 2;
        int textY = getHeight() / 2 + mTextBound.height() / 2;
        canvas.drawText(mText, textX, textY
                , mBackPaint);
        if (!mComplete) {
            // 画path
            drawPath();
            // 之前都是把内容画到bitmap上，现在需要把bitmap画到canvas上，否则不会出现在当前view上
            // 先画到图片上，再一块画到cavas上，有点之前开发游戏时的双缓冲效果。
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }

        if (mComplete)
        {
            if (mListener != null)
            {
                mListener.complete();
            }
        }
    }

    private void drawGrayBg() {
//        mCanvas.drawColor(Color.parseColor("#c0c0c0"));
        // 画背景时时设置为实心
        mOutterPaint.setStyle(Paint.Style.FILL);
        mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
        mCanvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30,
                mOutterPaint);
        mCanvas.drawBitmap(mOutterBitmap, null, new Rect(0, 0, width, height),
                null);
    }

    private void drawPath() {
        // 画path时设置为空心
        mOutterPaint.setStyle(Paint.Style.STROKE);
        // 设置过度时的mode
        mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath, mOutterPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                mPath.moveTo(mLastX, mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int dx = Math.abs(x - mLastX);
                int dy = Math.abs(y - mLastY);

                if (dx > 3 || dy > 3) {
                    mPath.lineTo(x, y);
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (!mComplete)
                    new Thread(mRunnable).start();
                break;
        }
        if (!mComplete)
            invalidate();
        return true;
    }

    /**
     * 设置我们绘制获奖信息的画笔属性
     */
    private void setUpBackPaint() {
        mBackPaint.setColor(mTextColor);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setTextSize(mTextSize);
        // 获得当前画笔绘制文本的宽和高
        mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
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

    // 判断遮盖层区域是否消除达到阈值
    // volatile关键字用于声明简单类型变量，如int、float、 boolean等数据类型。
    // 如果这些简单数据类型声明为volatile，对它们的操作就会变成原子级别的。但这有一定的限制。
    // 如下的表达式都不是原子操作：
    // n  =  n  +   1 ;
    // n ++ ;
    // 原因是声明为volatile的简单变量如果当前值由该变量以前的值相关，那么volatile关键字不起作用
    private volatile boolean mComplete = false;

    /**
     * 刮刮卡刮完的回调
     *
     * @author zhy
     */
    public interface OnGuaGuaKaCompleteListener {
        void complete();
    }
    private OnGuaGuaKaCompleteListener mListener;

    public void setOnGuaGuaKaCompleteListener(
            OnGuaGuaKaCompleteListener mListener)
    {
        this.mListener = mListener;
    }

    private Runnable mRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            int w = getWidth();
            int h = getHeight();

            float wipeArea = 0;
            float totalArea = w * h;
            Bitmap bitmap = mBitmap;
            int[] mPixels = new int[w * h];

            // 获得Bitmap上所有的像素信息
            bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

            for (int i = 0; i < w; i++)
            {
                for (int j = 0; j < h; j++)
                {
                    int index = i + j * w;
                    if (mPixels[index] == 0)
                    {
                        wipeArea++;
                    }
                }
            }

            if (wipeArea > 0 && totalArea > 0)
            {
                int percent = (int) (wipeArea * 100 / totalArea);

                if (percent > 60)
                {
                    // 清除掉图层区域
                    mComplete = true;
                    postInvalidate();

                }

            }

        }
    };
}
