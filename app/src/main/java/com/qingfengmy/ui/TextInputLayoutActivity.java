package com.qingfengmy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/6/25.
 */
public class TextInputLayoutActivity extends BaseActivity {
    @InjectView(R.id.text_input_layout)
    TextInputLayout mTextInputLayout;
    @InjectView(R.id.text_input_layout1)
    TextInputLayout mTextInputLayout1;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textinputlayout);
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

    @OnClick(R.id.floating_action_buton)
    public void clickFloatButton() {
        mTextInputLayout.setError("姓名输入错误！");
        mTextInputLayout.setErrorEnabled(true);
        mTextInputLayout1.setError("手机号输入错误！");
        mTextInputLayout1.setErrorEnabled(true);
    }

    @OnClick(R.id.floating_action_buton2)
    public void clickFloatButton2() {
        final Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "呼叫火星人？", Snackbar.LENGTH_LONG);
        snackbar.setAction("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
    @OnClick(R.id.floating_action_buton3)
    public void clickFloatButton3() {
        Snackbar.make(findViewById(R.id.coordinator_layout), "多么痛的领悟", Snackbar.LENGTH_LONG).show();
    }

}
