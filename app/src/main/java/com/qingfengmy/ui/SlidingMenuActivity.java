package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.AboutFragment;
import com.qingfengmy.ui.fragment.MenuFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-30
 * Time: 09:48
 */
public class SlidingMenuActivity extends BaseActivity implements MenuFragment.NavigationDrawerCallbacks{

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingmenu);
        ButterKnife.inject(this);

        titleBar.setTitle(getName(this));
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
        ft.replace(R.id.menu_frame, new MenuFragment());
        ft.commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        showToast(position+"被点击了");
    }
}
