package com.qingfengmy.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.RecyclerAdapter;
import com.qingfengmy.ui.view.AbsListViewScrollDetector;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        ButterKnife.inject(this);

        titleBar.setTitle(names[2]);
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerAdapter adapter = new RecyclerAdapter(this, "recyclerview's adapter");
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new AbsListViewScrollDetector() {
            @Override
            public void onScrollUp() {
                hideFloatButton();
            }

            @Override
            public void onScrollDown() {
                showFloatButton();
            }
        });

    }

    @OnClick(R.id.button)
    public void clickButton() {
        showToast("float button was clicked");
    }

    public void showFloatButton() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons, "translationY", 0, 300);
        animator.setDuration(300);
        animator.setInterpolator(new AnticipateInterpolator());
        animator.start();
    }

    public void hideFloatButton() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(buttons, "translationY", 300, 0);
        animator.setDuration(300);
        animator.setInterpolator(new AnticipateInterpolator());
        animator.start();
    }
}
