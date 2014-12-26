package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.qingfengmy.R;

/**
 * 所有activity的父类
 *
 * @author zhangtao(qingfengmy@126.com)
 *         <p/>
 *         2014-3-15
 */
public class BaseActivity extends ActionBarActivity {

    protected String[] names;
    protected String[] classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        names = getResources().getStringArray(R.array.names);
        classes = getResources().getStringArray(R.array.classes);
    }


    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
