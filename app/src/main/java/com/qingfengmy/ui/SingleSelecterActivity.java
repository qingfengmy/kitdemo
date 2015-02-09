package com.qingfengmy.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.AboutFragment;
import com.qingfengmy.ui.fragment.MainFragment;
import com.qingfengmy.ui.view.SprinnerView;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SingleSelecterActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.store_title)
    TextView storeTitle;
    @InjectView(R.id.view_content)
    FrameLayout viewContent;

    private List<String> titles;
    boolean show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigleselecter);
        ButterKnife.inject(this);
        Slidr.attach(this);

        titleBar.setTitle("");
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, new AboutFragment());
        ft.commit();


        titles = new ArrayList<String>();
        for(int i=0; i<15; i++){
            titles.add("名称--"+i);
        }
        SprinnerView sView = new SprinnerView(this, titles);
        sView.setSingleCallBackListener(new SprinnerView.SingleCallBackListenner() {
            @Override
            public void singleClick(int position) {
                clickTitle();
                storeTitle.setText(titles.get(position));
            }
        });
        viewContent.addView(sView);
    }

    @OnClick(R.id.store_title)
    public void clickTitle() {
        show = !show;
        anim(show);
    }

    public void anim(boolean b) {
        if (b) {
            // true in
            viewContent.setVisibility(View.VISIBLE);
            Animator anim = ObjectAnimator.ofFloat(viewContent, "translationY", -viewContent.getHeight(), 0);
            anim.setDuration(300);
            anim.start();
        } else {
            // false out
            Animator anim = ObjectAnimator.ofFloat(viewContent,"translationY", 0, -viewContent.getHeight());
            anim.setDuration(300);
           anim.start();
        }
    }
}
