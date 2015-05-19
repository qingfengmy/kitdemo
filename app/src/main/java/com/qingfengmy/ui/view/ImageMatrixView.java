package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.qingfengmy.R;

/**
 * Created by Administrator on 2015/2/1 0001.
 */
public class ImageMatrixView extends View {

    private Bitmap mBitmap;
    private Matrix mMatrix;

    public ImageMatrixView(Context context) {
        super(context);
        initView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        setImageMatrix(new Matrix());
    }

    public void setImageMatrix(Matrix matrix) {
        mMatrix = matrix;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画默认图片
        canvas.drawBitmap(mBitmap, 0, 0, null);
        // 画修改后的图片
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}
