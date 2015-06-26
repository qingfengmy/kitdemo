package com.qingfengmy.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.AboutFragment;
import com.qingfengmy.ui.fragment.GameFragment;
import com.qingfengmy.ui.fragment.JokeFragment;
import com.qingfengmy.ui.fragment.JokeImageFragment;
import com.qingfengmy.ui.fragment.LollipopFragment;
import com.qingfengmy.ui.fragment.MNCFragment;
import com.qingfengmy.ui.fragment.MainFragment;
import com.qingfengmy.ui.fragment.MenuFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements MenuFragment.NavigationDrawerCallbacks, NavigationView.OnNavigationItemSelectedListener {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;
    FragmentManager fragmentManager;

    Fragment mainFragment;
    Fragment aboutFragment;
    Fragment jokeFragment;
    Fragment jokeImgFragment;
    Fragment lollipopFragment;
    Fragment mncFragment;
    Fragment gameFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        setSupportActionBar(titleBar);
        titleBar.setTitle(R.string.app_name);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);

        fragmentManager = getSupportFragmentManager();

        // v7包中分装了其他东西，这里需要处理开关的监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, titleBar, R.string.open, R.string.close);

        // 设置drawer触发器为DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(this);

        mainFragment = new MainFragment();
        aboutFragment = new AboutFragment();
        jokeFragment = new JokeFragment();
        jokeImgFragment = new JokeImageFragment();
        lollipopFragment = new LollipopFragment();
        mncFragment = new MNCFragment();
        gameFragment = new GameFragment();
        selectItem(R.id.nav_home);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 将drawerToggle和drawerlayout同步
        // 将actionbarDrawerToogle中的图标设置为ActionBar中的home-button
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 切换屏幕更新toggle状态
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int itemId) {
        switch (itemId) {
            case R.id.nav_home:
                // 首页主程序
                if (switchFragment(mainFragment))
                setTitle("KitDemo");
                break;
            case R.id.nav_about:
                // 关于
                if (switchFragment(aboutFragment))
                setTitle("关于");
                break;
            case R.id.nav_joke:
                //笑话
                if (switchFragment(jokeFragment))
                setTitle("笑话");
                break;
            case R.id.nav_joke_img:
                // 趣图
                if (switchFragment(jokeImgFragment))
                setTitle("趣图");
                break;
            case R.id.nav_android_l:
                // android 5.0
                if (switchFragment(lollipopFragment))
                    setTitle("android L");
                break;
            case R.id.nav_android_m:
                // android 5.0
                if (switchFragment(mncFragment))
                    setTitle("android M");
                break;
            case R.id.nav_game:
                // 小游戏
                if (switchFragment( gameFragment))
                setTitle("小游戏");
                break;
        }

        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    private boolean switchFragment(Fragment to) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (currentFragment == to)
            return false;
        if (to.isAdded()) {
            if (currentFragment == null) {
                ft.show(to);
            } else {
                ft.show(to).hide(currentFragment);
            }
        } else {
            if (currentFragment == null) {
                ft.add(R.id.content_frame, to).show(to);
            } else {
                ft.hide(currentFragment).add(R.id.content_frame, to).show(to);
            }
        }
        ft.commit();
        currentFragment = to;
        return true;
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
//            openWebPage(getString(R.string.aboutme));
        } else if (item.getItemId() == R.id.menu_right) {
            openRightLayout();
        }
        return super.onOptionsItemSelected(item);
    }

    // 右边菜单开关事件
    public void openRightLayout() {

        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {

            mDrawerLayout.closeDrawer(Gravity.RIGHT);

        } else {

            mDrawerLayout.openDrawer(Gravity.RIGHT);

        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        selectItem(menuItem.getItemId());
        return true;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }
}
