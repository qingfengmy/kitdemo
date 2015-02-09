package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.GuaGuaKa;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrInterface;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-25
 * Time: 17:50
 */
public class GuaGuakaActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.rubbler)
    GuaGuaKa guaGuaKa;
    SlidrInterface slidrInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaguaka);
        ButterKnife.inject(this);
        slidrInterface = Slidr.attach(this);

        // 先设置title，再设置action，否则无效
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        guaGuaKa.setOnGuaGuaKaCompleteListener(new GuaGuaKa.OnGuaGuaKaCompleteListener() {
            @Override
            public void complete() {
                showToast("恭喜中大奖，好运滚滚来！");
            }
        });
        guaGuaKa.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    slidrInterface.lock();
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP){
                    slidrInterface.unlock();
                }
                return false;
            }
        });
    }

}
