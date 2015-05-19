package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.qingfengmy.R;

public class MeshView extends View {

    private int WIDTH = 200;
    private int HEIGHT = 200;
    // 总网格数
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    // 总网格的坐标值
    private float[] verts = new float[COUNT * 2];
    // 起始的网格坐标值
    private float[] orig = new float[COUNT * 2];
    private Bitmap mBitmap;
    private float K = 1;

    public MeshView(Context context) {
        super(context);
        initView();
    }

    public MeshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        int index = 0;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        float bmWidth = mBitmap.getWidth();
        float bmHeight = mBitmap.getHeight();

        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                orig[index * 2 + 0] = verts[index * 2 + 0] = fx;
                orig[index * 2 + 1] = verts[index * 2 + 1] = fy + 200;
                index += 1;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                // x坐标
                verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0;

                float offsetY = (float) Math.sin((float) j / WIDTH * 2 * Math.PI + K * 2 * Math.PI);
                // y坐标
                verts[(i * (WIDTH + 1) + j) * 2 + 1] =
                        orig[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 50;
            }
        }
        K += 0.1F;
        // 根据网格画图片
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT,
                verts, 0, null, 0, null);
        invalidate();
    }
}
