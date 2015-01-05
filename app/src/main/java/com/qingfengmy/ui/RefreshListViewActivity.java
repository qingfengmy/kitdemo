package com.qingfengmy.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.ApplicationAdapter;
import com.qingfengmy.ui.entity.AppInfo;
import com.qingfengmy.ui.view.RefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * User: zhangtao
 * Date: 2014-12-31
 * Time: 10:35
 */
public class RefreshListViewActivity extends BaseActivity implements RefreshListView.IReflashListener {

    private List<AppInfo> applicationList;
    private ApplicationAdapter mAdapter;

    @InjectView(R.id.refreshListView)
    RefreshListView listView;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshlistview);
        ButterKnife.inject(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new InitializeApplicationsTask().execute();
    }

    @Override
    public void onReflash() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取最新数据
                setReflashData();
                //通知界面显示
                mAdapter.notifyDataSetChanged();
                //通知listview 刷新数据完毕；
                listView.reflashComplete();
            }
        }, 2000);
    }

    private void setReflashData() {
        for (int i = 0; i < 2; i++) {
            AppInfo entity = applicationList.get(i);
            applicationList.add(entity);
        }
    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Query the applications
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

            applicationList = new ArrayList<>();

            List<ResolveInfo> ril = getPackageManager().queryIntentActivities(mainIntent, 0);
            for (ResolveInfo ri : ril) {
                applicationList.add(new AppInfo(RefreshListViewActivity.this, ri));
            }
            Collections.sort(applicationList);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            mAdapter = new ApplicationAdapter(RefreshListViewActivity.this, applicationList);
            listView.setAdapter(mAdapter);
            listView.setInterface(RefreshListViewActivity.this);

            Animation fadeIn = AnimationUtils.loadAnimation(RefreshListViewActivity.this, android.R.anim.slide_in_left);
            fadeIn.setDuration(250);
            LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
            listView.setLayoutAnimation(layoutAnimationController);
            listView.startLayoutAnimation();

            super.onPostExecute(result);
        }
    }
}
