package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/5/19.
 */
public class ImageMatirxActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_matrix);
        ButterKnife.inject(this);
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // 通过bitmapmesh，网格划分图片后，修改图片色素
    public void btnMesh(View view) {
        startActivity(new Intent(this, MeshViewTestActivity.class));
    }

    // 镜面效果
    public void btnReflect(View view) {
        startActivity(new Intent(this, ReflectViewTestActivity.class));
    }

    // 通过bitmapShader，实现圆角图片
    public void btnShader(View view) {
        startActivity(new Intent(this, BitmapShaderTestActivity.class));
    }

    // 通过xfermode实现圆角图片，xfermode仅支持拥有rgba通道的图片
    public void btnXfermode(View view) {
        startActivity(new Intent(this, RoundRectXfermodTestActivity.class));
    }

    // 矩阵
    public void btnMatrix(View view) {
        startActivity(new Intent(this, ImageMatrixTestActivity.class));
    }
}
