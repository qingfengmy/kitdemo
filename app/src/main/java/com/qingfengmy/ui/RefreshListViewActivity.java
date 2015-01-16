package com.qingfengmy.ui;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.ApplicationAdapter;
import com.qingfengmy.ui.adapters.MyAdapter;
import com.qingfengmy.ui.adapters.RecyclerAdapter;
import com.qingfengmy.ui.entity.AppInfo;
import com.qingfengmy.ui.view.EmptyView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * User: zhangtao
 * Date: 2014-12-31
 * Time: 10:35
 */
public class RefreshListViewActivity extends BaseActivity {

    private List<AppInfo> applicationList;
    private MyAdapter mAdapter;

    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    List<String> titles;

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

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new InitializeApplicationsTask().execute();
            }
        });


        ListView actualListView = mPullRefreshListView.getRefreshableView();

        actualListView.setEmptyView(new EmptyView(this));

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);

        // init date
        titles = new ArrayList<>();

        mAdapter = new MyAdapter(RefreshListViewActivity.this, titles);

        actualListView.setAdapter(mAdapter);

        mPullRefreshListView.setRefreshing();
    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }

            for (int i = 0; i < 20; i++)
                titles.add("recyclerview's adapter");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            titles.add("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
}
