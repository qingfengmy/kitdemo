package com.qingfengmy.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.SampleAdapter;
import com.qingfengmy.ui.view.EmptyView;
import com.qingfengmy.ui.view.SampleDivider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/19.
 */
public class SwipeRefreshActivity extends BaseActivity {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    LinkedList<String> titles;
    private SampleAdapter mAdapter;

    private int lastVisibleItem;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(SwipeRefreshActivity.this, "DOWN", Toast.LENGTH_LONG).show();
                    mSwipeLayout.setRefreshing(false);

                    mAdapter.getList().clear();
                    addList();
                    break;
                case 1:
                    Toast.makeText(SwipeRefreshActivity.this, "UP", Toast.LENGTH_LONG).show();
                    addList();
                    break;
                default:
                    break;
            }

        }

    };
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
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    handler.sendEmptyMessageDelayed(1, 3000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new SampleDivider(this));

        mAdapter = new SampleAdapter();
        mRecyclerView.setAdapter(mAdapter);

        addList();

    }

    private void addList() {
        List<Integer> list = getList();
        mAdapter.getList().addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private List<Integer> getList() {
        List<Integer> list = new ArrayList<Integer>();
        int size = mAdapter.getList().size();
        int lastPosition = size > 0 ? mAdapter.getList().get(size - 1) : 0;
        for (int i = 1; i < 20; i++) {
            list.add(lastPosition + i);
        }

        return list;
    }
}
