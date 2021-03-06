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
public class SingleTopActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.text)
    TextView text_show;
    @InjectView(R.id.contnet)
    TextView contnet;

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
        contnet.setText("如果在任务的栈顶正好存在该Activity的实例， 就重用该实例，否者就会创建新的实例并放入栈顶(即使栈中已经存在该Activity实例，只要不在栈顶，都会创建实例)。");
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
