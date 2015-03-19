package com.qingfengmy.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.RecyclerAdapterHideOnScroll;
import com.qingfengmy.ui.listenner.HidingScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/19.
 * http://jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0317/2612.html
 * https://github.com/mzgreen/HideOnScrollExample
 */
public class HideOnScrollActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.fabButton)
    ImageButton mFabButton;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hideonscroll);
        ButterKnife.inject(this);

        initToolbar();
        initRecyclerView();
    }

    private void initToolbar() {
        mToolbar.setTitle(getName(this));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initRecyclerView() {
        list = new ArrayList<>();
        for(int i=0; i<20; i++){
            list.add("aaa_"+i+"_bbb");
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapterHideOnScroll recyclerAdapter = new RecyclerAdapterHideOnScroll(list);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        //我们需要将margin也计算进去，不然fab不能完全隐藏
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }
}
