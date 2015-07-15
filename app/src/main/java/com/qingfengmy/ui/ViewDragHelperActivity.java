package com.qingfengmy.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.qingfengmy.R;
import com.qingfengmy.databinding.ActivityDraghelperBinding;

/**
 * Created by Administrator on 2015/7/15.
 * 参考：
 * http://blog.csdn.net/lmj623565791/article/details/46858663
 */
public class ViewDragHelperActivity extends BaseActivity {

    ActivityDraghelperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_draghelper);
        binding.toolbar.setTitle(getName(this));
        setSupportActionBar(binding.toolbar);
    }
}
