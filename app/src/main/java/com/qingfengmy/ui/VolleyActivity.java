package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.VolleyFirstFragment;
import com.qingfengmy.ui.fragment.VolleySecondFragment;
import com.qingfengmy.ui.fragment.VolleyThirdFragment;
import com.r0adkll.slidr.Slidr;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/15.
 */
public class VolleyActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    VolleyFirstFragment firstFragment;
    VolleySecondFragment secondFragment;
    VolleyThirdFragment thirdFragment;

    FragmentManager fm;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
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

        // 初始化fragmentManager
        fm = getSupportFragmentManager();
        firstFragment = new VolleyFirstFragment();
        secondFragment = new VolleySecondFragment();
        thirdFragment = new VolleyThirdFragment();

        /* 注意这里拿到的RequestQueue是一个请求队列对象，它可以缓存所有的HTTP请求，
         然后按照一定的算法并发地发出这些请求。RequestQueue内部的设计就是非常合适高并发的，
         因此我们不必为每一次HTTP请求都创建一个RequestQueue对象，这是非常浪费资源的，
         基本上在每一个需要和网络交互的Activity中创建一个RequestQueue对象就足够了。*/
        mQueue = getRequestQueue();
        Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_volley, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentTransaction ft = fm.beginTransaction().setCustomAnimations(R.anim.push_in, R.anim.push_out);
        switch (item.getItemId()) {
            case R.id.action_first:
                if (firstFragment.isAdded()) {
                    ft.hide(secondFragment).hide(thirdFragment).show(firstFragment).commit();
                } else {
                    ft.hide(secondFragment).hide(thirdFragment).add(R.id.volley_content, firstFragment).commit();
                }
//                ft.replace(R.id.volley_content, firstFragment).commit();
                break;
            case R.id.action_second:
                if (secondFragment.isAdded()) {
                    ft.hide(firstFragment).hide(thirdFragment).show(secondFragment).commit();
                } else {
                    ft.hide(firstFragment).hide(thirdFragment).add(R.id.volley_content, secondFragment).show(secondFragment).commit();
                }
//                ft.replace(R.id.volley_content, secondFragment).commit();
                break;
            case R.id.action_third:
                if (thirdFragment.isAdded()) {
                    ft.hide(secondFragment).hide(firstFragment).show(thirdFragment).commit();
                } else {
                    ft.hide(secondFragment).hide(firstFragment).add(R.id.volley_content, thirdFragment).show(thirdFragment).commit();
                }
//                ft.replace(R.id.volley_content, thirdFragment).commit();
                break;
        }
        return true;
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null)
            mQueue = Volley.newRequestQueue(this);
        return mQueue;
    }
}