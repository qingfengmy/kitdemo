package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.BaiDuLoadingView;
import com.qingfengmy.ui.view.SouGouLoadingView;

public class LoadingActivity extends AppCompatActivity {

    SouGouLoadingView souGouLoadingView;
    BaiDuLoadingView baiDuLoadingView;
    Toolbar toolbar;
    boolean isBaiDuShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("LoadingView");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        souGouLoadingView = (SouGouLoadingView) findViewById(R.id.sogou);
        baiDuLoadingView = (BaiDuLoadingView) findViewById(R.id.baidu);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeView();
            }
        });

        changeView();
    }

    private void changeView() {
        if (isBaiDuShow){
            baiDuLoadingView.setVisibility(View.VISIBLE);
            souGouLoadingView.setVisibility(View.GONE);
        }else {
            baiDuLoadingView.setVisibility(View.GONE);
            souGouLoadingView.setVisibility(View.VISIBLE);
        }
        isBaiDuShow = !isBaiDuShow;
    }

}
