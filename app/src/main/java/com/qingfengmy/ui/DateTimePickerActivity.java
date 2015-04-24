package com.qingfengmy.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.DatePickerFragment;
import com.qingfengmy.ui.fragment.TimePickerFragment;
import com.qingfengmy.ui.utils.tools.ToastUtil;
import com.qingfengmy.ui.view.FeedContextMenu;
import com.qingfengmy.ui.view.FeedContextMenuManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/4/15.
 */
public class DateTimePickerActivity extends BaseActivity implements FeedContextMenu.OnFeedContextMenuItemClickListener {
    @InjectView(R.id.container)
    FrameLayout layout;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    Fragment dateFragment, timeFragment, currentFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datetime);
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

        dateFragment = new DatePickerFragment();
        timeFragment = new TimePickerFragment();
        fm = getFragmentManager();
        switchContent(dateFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_date:
                FeedContextMenuManager.getInstance(this).toggleContextMenuFromView(titleBar, this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment) {
        if (currentFragment == fragment)
            return;
        FragmentTransaction transaction = fm.beginTransaction().setCustomAnimations(R.anim.push_in, R.anim.push_out);
        if (!fragment.isAdded()) {
            if (currentFragment != null)
                transaction.hide(currentFragment).add(R.id.container, fragment).commit();
            else
                transaction.add(R.id.container, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }


    @Override
    public void onReportClick() {
        FeedContextMenuManager.getInstance(this).hideContextMenu();
        ToastUtil.showToast(this, "first");
    }

    @Override
    public void onSharePhotoClick() {
        FeedContextMenuManager.getInstance(this).hideContextMenu();
        ToastUtil.showToast(this, "second");
    }
}
