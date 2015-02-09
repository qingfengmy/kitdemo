package com.qingfengmy.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.RecyclerAdapter;
import com.qingfengmy.ui.view.AbsListViewScrollDetector;
import com.qingfengmy.ui.view.SampleDivider;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 17:19
 */
public class RecyclerActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    @InjectView(R.id.buttons)
    RelativeLayout buttons;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    List<String> titles;
    RecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
    private boolean show = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.inject(this);
        Slidr.attach(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //  创建列表项分隔线对象final
        RecyclerView.ItemDecoration itemDecoration = new SampleDivider(this);
        //  为RecyclerView控件指定分隔线对象
        recyclerView.addItemDecoration(itemDecoration);


        titles = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            titles.add("recyclerview's adapter");
        adapter = new RecyclerAdapter(this, titles);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem 是从0开始数的，totalItemCount是从1开始数的，故total-1
                // dy>0 表示向上滑动
                if (lastVisibleItem == totalItemCount - 1 && dy > 0) {
                    adapter.add("last's adapter", lastVisibleItem);
                }
                Log.e("aaa", "dy=" + dy);
                Log.e("aaa", "show=" + show);
                if (dy < -10) {
                    if (!show)
                        showFloatButton();
                } else if (dy > 10) {
                    if (show)
                        hideFloatButton();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add("new's adapter", 0);
                        mSwipeLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }

    @OnClick(R.id.button)
    public void clickButton() {
        showToast("float button was clicked");
    }

    public void showFloatButton() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons, "translationY", 300, 0);
        animator.setDuration(300);
//        animator.setInterpolator(new AnticipateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                buttons.setVisibility(View.VISIBLE);
                show = true;
            }
        });
        animator.start();
    }

    public void hideFloatButton() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons, "translationY", 0, 300);
        animator.setDuration(300);
//        animator.setInterpolator(new AnticipateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                show = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttons.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }
}
