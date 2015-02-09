package com.qingfengmy.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.StickyHeaderFragment;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.SlidrInterface;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/26.
 */
public class StickyHeaderActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    SlidrInterface slidrInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stickyheader);
        ButterKnife.inject(this);
        slidrInterface = Slidr.attach(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new StickyHeaderFragment()).commit();
    }

    public SlidrInterface getSlidrInterface() {
        return slidrInterface;
    }
}
