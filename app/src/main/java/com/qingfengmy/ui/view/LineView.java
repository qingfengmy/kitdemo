package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 阳光小强 http://blog.csdn.net/dawanganban
 */
public class LineView extends View {

    // 画xy轴的画笔
    private Paint xyPaint;
    // 画数据线的画笔
    private Paint dataPaint;
    // 画刻度线
    private Paint calibrationPaint;
    // 画虚线的画笔
    private Paint dotLinePaint;
    // x轴的刻度值
    private List<String> xCalibrations;
    // y轴的刻度值
    private List<Integer> yCalibrations;
    // 数据值
    private List<Point> data = new ArrayList<Point>();
    // 控件宽度高度
    private int width, height;
    // 是否第一次
    private boolean once = true;
    // 左右留得距离
    private int padding;
    // xy原点
    private int xOrigin, yOrigin, xEnd, yEnd;

    private float xDot, yDot;

    private List<Point> points;

    private int XPoint = 60;
    private int YPoint = 260;
    private int XScale = 8; // 刻度长度
    private int YScale = 40;
    private int XLength = 380;
    private int YLength = 240;
    // 100和实际高度的比例
    private float b;

    private int MaxDataSize = XLength / XScale;


    private String[] YLabel = new String[YLength / YScale];

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initData();
    }

    private void initData() {
        xCalibrations = new ArrayList<>();
        xCalibrations.add("00:00");
        xCalibrations.add("02:00");
        xCalibrations.add("04:00");
        xCalibrations.add("06:00");
        xCalibrations.add("08:00");
        xCalibrations.add("10:00");
        xCalibrations.add("12:00");
        xCalibrations.add("14:00");
        xCalibrations.add("16:00");
        xCalibrations.add("18:00");
        xCalibrations.add("20:00");
        xCalibrations.add("22:00");

        data.add(new Point(0, 200));
        data.add(new Point(1, 300));
        data.add(new Point(2, 100));
        data.add(new Point(3, 400));
        data.add(new Point(4, 300));
        data.add(new Point(5, 500));
        data.add(new Point(6, 0));
        data.add(new Point(7, 100));
        data.add(new Point(8, 200));
        data.add(new Point(9, 300));
        data.add(new Point(10, 400));
        data.add(new Point(11, 500));
        data.add(new Point(12, 260));
        data.add(new Point(13, 280));
        data.add(new Point(14, 290));
        data.add(new Point(15, 160));
        data.add(new Point(16, 260));
        data.add(new Point(17, 350));
        data.add(new Point(18, 360));
        data.add(new Point(19, 460));
        data.add(new Point(20, 460));
        data.add(new Point(21, 160));
        data.add(new Point(22, 260));
        data.add(new Point(23, 360));
    }

    // 初始化 画笔
    private void initPaint() {
        xyPaint = new Paint();
        xyPaint.setColor(Color.BLUE);
        // 抗锯齿
        xyPaint.setAntiAlias(true);
        // 防抖动
        xyPaint.setDither(true);
        // 画笔外轮廓
        xyPaint.setStrokeJoin(Paint.Join.ROUND);
        xyPaint.setStrokeCap(Paint.Cap.ROUND);
        xyPaint.setStyle(Paint.Style.FILL);
        xyPaint.setStrokeWidth(5);

        calibrationPaint = new Paint();
        calibrationPaint.setColor(Color.BLACK);
        calibrationPaint.setAntiAlias(true);
        calibrationPaint.setDither(true);
        calibrationPaint.setTextSize(22);

        dataPaint = new Paint();
        dataPaint.setColor(Color.RED);
        dataPaint.setAntiAlias(true);
        dataPaint.setDither(true);
        dataPaint.setStrokeJoin(Paint.Join.ROUND);
        dataPaint.setStrokeCap(Paint.Cap.ROUND);
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setStrokeWidth(6);

        dotLinePaint = new Paint();
        dotLinePaint.setColor(Color.GRAY);
        dotLinePaint.setAntiAlias(true);
        dotLinePaint.setDither(true);
        dotLinePaint.setStrokeJoin(Paint.Join.ROUND);
        dotLinePaint.setStrokeCap(Paint.Cap.ROUND);
        dotLinePaint.setStyle(Paint.Style.STROKE);
        dotLinePaint.setStrokeWidth(2);
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        dotLinePaint.setPathEffect(effects);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏幕宽度
        width = getMeasuredWidth();
        height = width * 3 / 4;
        if (once) {
            padding = width / 10;
            xOrigin = padding;
            yOrigin = height - padding;
            xEnd = width - padding;
            yEnd = padding;

            xDot = new BigDecimal(width - padding * 2).divide(new BigDecimal(24)).floatValue();

            yCalibrations = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                yCalibrations.add(data.get(i).getY());
            }
            int yy =max(yCalibrations) / 100+1;
            BigDecimal bd = new BigDecimal(height - padding * 2);
            yDot = bd.divide(new BigDecimal(yy)).floatValue();
            b = new BigDecimal(yDot).divide(new BigDecimal(100)).floatValue();
            yCalibrations.clear();
            for (int i = 0; i <= yy; i++) {
                yCalibrations.add(100 * i);
            }

            points = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                int x = (int) (xDot * i);
                int y = data.get(i).getY();
                points.add(new Point(x, y));
            }
            once = false;
        }
        // 设置控件宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画xy方向的测量线（虚线）
        drawXYDottedLine(canvas);
        // 画xy轴
        drawXYLine(canvas);
        // 画xy轴刻度值
        drawXYValues(canvas);
        // 画数据值
        drawData(canvas);
    }
    private void drawXYDottedLine(Canvas canvas) {
        // x方向
        for (int i = 0; i < points.size()+1; i++) {
            int x = (int) (xOrigin + xDot * i);
            canvas.drawLine(x, yOrigin, x, yEnd, dotLinePaint);
        }
        // y方向
        for (int i = 0; i < yCalibrations.size(); i++) {
            int y = (int) (yOrigin - yDot * i);
            canvas.drawLine(xOrigin, y, xEnd, y, dotLinePaint);
        }
    }

    // 画数据值
    private void drawData(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            Point p = convert(points.get(i));
            if (i == 0)
                path.moveTo(p.getX(), p.getY());
            else
                path.lineTo(p.getX(), p.getY());

            // 以point为原点画实心圆
            canvas.drawCircle(p.getX(), p.getY(), 6, dataPaint);
        }
        canvas.drawPath(path, dataPaint);
    }

    // 画xy轴刻度值
    private void drawXYValues(Canvas canvas) {
        for (int i = 0; i < xCalibrations.size(); i++) {
            canvas.drawText(xCalibrations.get(i), xOrigin + xDot * 2 * i - 30, yOrigin + 20, calibrationPaint);
            canvas.drawCircle(xOrigin + xDot * 2 * i, yOrigin, 5, xyPaint);
        }
        for (int i = 0; i < yCalibrations.size(); i++) {
            canvas.drawText(yCalibrations.get(i) + "", xOrigin - 40, yOrigin - yDot * i, calibrationPaint);
        }
    }

    // 画xy轴
    private void drawXYLine(Canvas canvas) {
        canvas.drawLine(xOrigin, yOrigin, xEnd, yOrigin, xyPaint);
        canvas.drawLine(xOrigin, yOrigin, xOrigin, yEnd, xyPaint);
    }

    // 相对于原点的坐标，转换成相对于左上角的坐标
    private Point convert(Point p) {
        p.setX(xOrigin + p.getX());
        int y = (int) (yOrigin - p.getY()*b);
        p.setY(y);
        return p;
    }

    private int max(List<Integer> list) {
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i)) {
                max = list.get(i);
            }
        }
        return max;
    }

    private class Point {
        private int x;
        private int y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }
    }
}

