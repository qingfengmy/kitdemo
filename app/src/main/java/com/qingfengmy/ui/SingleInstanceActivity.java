package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/4/15.
 */
public class SingleInstanceActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.text)
    TextView text_show;
    @InjectView(R.id.contnet)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchmode);
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

        text_show.setText(this.toString());
        content.setText("在一个新栈中创建该Activity实例，并让多个应用共享改栈中的该Activity实例。一旦改模式的Activity的实例存在于某个栈中，任何应用再激活改Activity时都会重用该栈中的实例，其效果相当于多个应用程序共享一个应用，不管谁激活该Activity都会进入同一个应用中。\" ");
    }

    @OnClick(R.id.standard)
    public void startStanard() {
        startActivity(new Intent(this, LaunchModeActivity.class));
    }

    @OnClick(R.id.singleTop)
    public void startSingleTop() {
        startActivity(new Intent(this, SingleTopActivity.class));
    }

    @OnClick(R.id.singleTask)
    public void startSingleTask() {
        startActivity(new Intent(this, SingleTasksActivity.class));
    }

    @OnClick(R.id.singleInstance)
    public void startInstance() {
        startActivity(new Intent(this, SingleInstanceActivity.class));
    }
}
