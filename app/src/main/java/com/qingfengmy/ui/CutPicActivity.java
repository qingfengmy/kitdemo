package com.qingfengmy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.CutPicView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-29
 * Time: 13:30
 */
public class CutPicActivity extends BaseActivity {

    @InjectView(R.id.cut_pic_view)
    CutPicView mCutPicView;

    public static Bitmap bitmap;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutpic);
        ButterKnife.inject(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(names[5]);
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.demo);
        Bitmap bitmap = bd.getBitmap();
        mCutPicView.setBitmap(bitmap);
    }

    @OnClick(R.id.btn_done)
    public void done() {
        bitmap = mCutPicView.toRoundBitmap();
        Intent intent = new Intent(CutPicActivity.this, MainActivity.class);
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_done){
            bitmap = mCutPicView.toRoundBitmap();
            titleBar.setNavigationIcon(new BitmapDrawable(bitmap));
        }
        return super.onOptionsItemSelected(item);
    }
}
