package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/7/15.
 * 参考：
 * http://blog.csdn.net/lmj623565791/article/details/46858663
 */
public class ViewDragHelperActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draghelper);
        ButterKnife.inject(this);
        toolbar.setTitle(getName(this));
        setSupportActionBar(toolbar);
    }
}
