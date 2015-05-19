package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/5/19.
 */
public class MoreTextViewActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_text);
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
}
