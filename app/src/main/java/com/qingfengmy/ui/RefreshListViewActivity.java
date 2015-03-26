package com.qingfengmy.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.MyAdapter;
import com.qingfengmy.ui.view.EmptyView;
import com.qingfengmy.ui.view.LoadMoreListView;
import com.r0adkll.slidr.Slidr;

import java.util.LinkedList;
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

    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshlistview);
        ButterKnife.inject(this);
        Slidr.attach(this);
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

        // the following are default_image settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default_image is false
        mPtrFrame.setPullToRefresh(false);
        // default_image is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        actualListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (total < 10)
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
            int count = new Random().nextInt(20);
            for (int i = 0; i < count; i++)
                titles.add("recyclerview's adapter-" + new Random().nextInt(100));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();
            mPtrFrame.refreshComplete();
            actualListView.setCanLoadMore(true);
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
            if (total < 10) {
                titles.addLast("recyclerview's footview--");
            } else {
                showToast("mei you geng duo shu ju");
            }
            mAdapter.notifyDataSetChanged();
            actualListView.onLoadMoreComplete();
            actualListView.setCanLoadMore(total < 10);
            total++;
        }
    }
}
