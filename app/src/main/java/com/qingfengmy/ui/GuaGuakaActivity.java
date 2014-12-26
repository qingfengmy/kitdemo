package com.qingfengmy.ui;

import android.os.Bundle;

import com.qingfengmy.R;

import butterknife.ButterKnife;

/**
 * User: zhangtao
 * Date: 2014-12-25
 * Time: 17:50
 */
public class GuaGuakaActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guaguaka);
        ButterKnife.inject(this);

    }

}
