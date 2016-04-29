package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.FlowLayoutView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 10:04
 */
public class FlowLayoutActivity extends BaseActivity {
    private String[] mVals = new String[]
            {"杨过", "小龙女", "金轮法王 ", "郭靖", "黄蓉", "王重阳",
                    "九阴真经", "葵花宝典", "天地会", "地震高岗", "一派青山千古秀",
                    "韦小宝", "陈近南", "千年老妖", "东海龙王"};

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.flowlayout1)
    FlowLayoutView mFlowLayout1;
    @InjectView(R.id.flowlayout2)
    FlowLayoutView mFlowLayout2;
    @InjectView(R.id.flowlayout3)
    FlowLayoutView mFlowLayout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        ButterKnife.inject(this);

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

        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.text_flowlayout,
                    mFlowLayout1, false);
            tv.setText(mVals[i]);
            mFlowLayout1.addView(tv);
        }
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.text_flowlayout,
                    mFlowLayout2, false);
            tv.setText(mVals[i]);
            mFlowLayout2.addView(tv);
        }
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.text_flowlayout,
                    mFlowLayout3, false);
            tv.setText(mVals[i]);
            mFlowLayout3.addView(tv);
        }
    }
}
