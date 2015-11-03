package com.qingfengmy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.qingfengmy.R;
import com.qingfengmy.ui.utils.ClipPicture;
import com.r0adkll.slidr.Slidr;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PictureActivity extends BaseActivity {
    private ImageView iv_image = null;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    boolean from;
    private ClipPicture clip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.inject(this);
        Slidr.attach(this);
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);
        iv_image = (ImageView) this.findViewById(R.id.img);

        clip = new ClipPicture(this, ClipPicture.GET_BITMAP_AND_FILE) {
            @Override
            public void onFinish(Bitmap bitmap, File picture, String cache_name) {
                // 返回数据
                if (bitmap != null)
                iv_image.setImageBitmap(bitmap);
            }
        };

        this.findViewById(R.id.camare).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 相机
                clip.clipPicture(ClipPicture.USE_CAMERA);
            }
        });

        this.findViewById(R.id.galary).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 相册
                clip.clipPicture(ClipPicture.USE_PHOTO);
            }
        });

        if (getIntent().getBooleanExtra("from", false)) from = true;
        else from = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clip.dealPictureResult(requestCode, resultCode, data);
    }
}
