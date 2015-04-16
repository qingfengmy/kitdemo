package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/4/15.
 */
public class SingleTasksActivity extends BaseActivity {
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
        contnet.setText("如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())。重用时，会让该实例回到栈顶，因此在它上面的实例将会被移除栈。如果栈中不存在该实例，将会创建新的实例放入栈中。");
        ToastUtil.showToast(this,"single task oncreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ToastUtil.showToast(this,"single task onNewIntent");
    }

    @OnClick(R.id.standard)
    public void startStanard() {
        startActivity(new Intent(this,LaunchModeActivity.class));
    }
    @OnClick(R.id.singleTop)
    public void startSingleTop(){
        startActivity(new Intent(this,SingleTopActivity.class));
    }
    @OnClick(R.id.singleTask)
    public void startSingleTask(){
        startActivity(new Intent(this,SingleTasksActivity.class));
    }

    @OnClick(R.id.singleInstance)
    public void startInstance() {
        startActivity(new Intent(this, SingleInstanceActivity.class));
    }
}
