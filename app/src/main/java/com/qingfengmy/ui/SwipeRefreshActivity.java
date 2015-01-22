package com.qingfengmy.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.MyAdapter;
import com.qingfengmy.ui.view.EmptyView;
import com.qingfengmy.ui.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/19.
 */
public class SwipeRefreshActivity extends BaseActivity {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.listview)
    LoadMoreListView actualListView;
    @InjectView(R.id.empty_view)
    EmptyView emptyView;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    List<String> titles;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperefresh);
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

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new InitTask().execute();
            }
        });


        actualListView.setLoadMoreListenner(new LoadMoreListView.LoadMoreListenner() {
            @Override
            public void loadMore() {
                new LoadMoreTask().execute();
            }
        });
        actualListView.setEmptyView(emptyView);
        // init date
        titles = new ArrayList<>();

        mAdapter = new MyAdapter(SwipeRefreshActivity.this, titles);

        actualListView.setAdapter(mAdapter);

        mSwipeLayout.setRefreshing(true);
    }

    private class InitTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }

            titles.add("recyclerview's refresh-");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            mSwipeLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    private class LoadMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            int i = new Random().nextInt(10)%2;
            if(i == 0)
                titles.add("recyclerview's footview--");
            else
                showToast("mei you geng duo shu ju");
            mAdapter.notifyDataSetChanged();
            actualListView.loadMoreComplete();
            super.onPostExecute(result);
        }
    }
}
