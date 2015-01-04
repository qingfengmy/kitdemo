package com.qingfengmy.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.AboutFragment;
import com.qingfengmy.ui.fragment.MainFragment;
import com.qingfengmy.ui.fragment.MenuFragment;

import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements MenuFragment.NavigationDrawerCallbacks{

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSwipeBackEnable(false);

        setSupportActionBar(titleBar);
        titleBar.setTitle(R.string.app_name);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);
        // v7包中分装了其他东西，这里需要处理开关的监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, titleBar, R.string.open, R.string.close);

        // 设置drawer触发器为DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(0);

        Log.e("aa","onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("aa","onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("aa","onWindowFocusChanged");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 将drawerToggle和drawerlayout同步
        // 将actionbarDrawerToogle中的图标设置为ActionBar中的home-button
        mDrawerToggle.syncState();
        Log.e("aa","onPostCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 切换屏幕更新toggle状态
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        switch (position) {
            case 0:
                // 首页主程序
                // 创建一个新的fragment并且根据行星的位置来显示
                Fragment fragment = new MainFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);

                // 通过替换已存在的fragment来插入新的fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                break;
            case 1:
                // 关于
                // 创建一个新的fragment并且根据行星的位置来显示
                fragment = new AboutFragment();
                // 通过替换已存在的fragment来插入新的fragment
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
                break;
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    public void setTitle(CharSequence title) {
        titleBar.setTitle(title);
    }

    long exitTime;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出应用程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_about) {
            String url = getString(R.string.aboutme);
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }
}
