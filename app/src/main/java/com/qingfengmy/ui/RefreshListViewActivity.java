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
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.ApplicationAdapter;
import com.qingfengmy.ui.adapters.MyAdapter;
import com.qingfengmy.ui.adapters.RecyclerAdapter;
import com.qingfengmy.ui.entity.AppInfo;
import com.qingfengmy.ui.view.EmptyView;
import com.qingfengmy.ui.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * User: zhangtao
 * Date: 2014-12-31
 * Time: 10:35
 */
public class RefreshListViewActivity extends BaseActivity {

    private MyAdapter mAdapter;

    @InjectView(R.id.list_view_with_empty_view_fragment_ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    @InjectView(R.id.list_view_with_empty_view_fragment_list_view)
    LoadMoreListView actualListView;

    @InjectView(R.id.list_view_with_empty_view_fragment_empty_view)
    EmptyView emptyView;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    LinkedList<String> titles;

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

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                new InitializeApplicationsTask().execute();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 第二个参数必须是listview，否则滑动会出错
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, actualListView, header);
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        actualListView.setLoadMoreListenner(new LoadMoreListView.LoadMoreListenner() {
            @Override
            public void loadMore() {
                new LoadMoreTask().execute();
            }
        });
        actualListView.setEmptyView(emptyView);
        // init date
        titles = new LinkedList<>();

        mAdapter = new MyAdapter(RefreshListViewActivity.this, titles);

        actualListView.setAdapter(mAdapter);

        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

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

            titles.clear();
            int count = 3;
//            int count = new Random().nextInt(20);
            for (int i = 0; i < count; i++)
                titles.add("recyclerview's adapter-" + new Random().nextInt(100));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            mPtrFrame.refreshComplete();
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
                titles.addLast("recyclerview's footview--");
            else
                showToast("mei you geng duo shu ju");
            mAdapter.notifyDataSetChanged();
            actualListView.loadMoreComplete();
            super.onPostExecute(result);
        }
    }
}
