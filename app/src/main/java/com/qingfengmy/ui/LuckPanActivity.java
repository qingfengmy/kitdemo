package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.LuckPanSurface;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/7/15.
 */
public class LuckPanActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.luckPanView)
    LuckPanSurface luckPanView;
    @InjectView(R.id.bton)
    ImageView bton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckpan);
        ButterKnife.inject(this);
        toolbar.setTitle("抽奖了");
        setSupportActionBar(toolbar);

        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!luckPanView.isStart()) {
                    luckPanView.luckyStart(1);
                    bton.setImageResource(R.drawable.stop);
                } else {
                    // 还在转
                    if (!luckPanView.isShouldEnd()) {
                        // 停止按钮还没按
                        luckPanView.luckyEnd();
                        bton.setImageResource(R.drawable.start);
                    }
                }
            }
        });
    }
}
