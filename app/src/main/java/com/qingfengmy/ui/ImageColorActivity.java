package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.r0adkll.slidr.Slidr;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/5/19.
 */
public class ImageColorActivity extends BaseActivity {



    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_color);
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


    public void btnPixelsEffect(View view) {
        startActivity(new Intent(this, PixelsEffectActivity.class));
    }

    public void btnColorMatrix(View view) {
        startActivity(new Intent(this, ColorMatrixActivity.class));
    }

    public void btnPrimaryColor(View view) {
        startActivity(new Intent(this, PrimaryColorActivity.class));
    }
}
