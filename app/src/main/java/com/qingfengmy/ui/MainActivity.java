package com.qingfengmy.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
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
import com.qingfengmy.ui.fragment.LollipopFragment;
import com.qingfengmy.ui.fragment.MainFragment;
import com.qingfengmy.ui.fragment.MenuFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements MenuFragment.NavigationDrawerCallbacks {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    FragmentManager fragmentManager;

    MainFragment mainFragment;
    AboutFragment aboutFragment;
    JokeFragment jokeFragment;
    JokeFragment jokeImgFragment;
    LollipopFragment lollipopFragment;
    GameFragment gameFragment;

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

        mainFragment = new MainFragment();
        aboutFragment = new AboutFragment();

        jokeFragment = new JokeFragment();
        Bundle jokeargs = new Bundle();
        jokeargs.putBoolean(JokeFragment.TYPE, true);
        jokeFragment.setArguments(jokeargs);

        jokeImgFragment = new JokeFragment();
        Bundle args = new Bundle();
        args.putBoolean(JokeFragment.TYPE, false);
        jokeImgFragment.setArguments(args);

        lollipopFragment = new LollipopFragment();

        gameFragment = new GameFragment();

        selectItem(0);

        Log.e("aa", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("aa", "onResume");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e("aa", "onWindowFocusChanged");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 将drawerToggle和drawerlayout同步
        // 将actionbarDrawerToogle中的图标设置为ActionBar中的home-button
        mDrawerToggle.syncState();
        Log.e("aa", "onPostCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 切换屏幕更新toggle状态
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                // 首页主程序
                if (mainFragment.isAdded()) {
                    ft.show(mainFragment).hide(aboutFragment).hide(jokeFragment).hide(gameFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                } else {
                    ft.add(R.id.content_frame, mainFragment).show(mainFragment).hide(gameFragment).hide(aboutFragment).hide(jokeFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                }
                break;
            case 1:
                // 关于
                if (aboutFragment.isAdded()) {
                    ft.show(aboutFragment).hide(mainFragment).hide(jokeFragment).hide(gameFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                } else {
                    ft.add(R.id.content_frame, aboutFragment).show(aboutFragment).hide(gameFragment).hide(mainFragment).hide(jokeFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                }
                break;
            case 2:
                //笑话
                if (jokeFragment.isAdded()) {
                    ft.show(jokeFragment).hide(mainFragment).hide(gameFragment).hide(aboutFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                } else {
                    ft.add(R.id.content_frame, jokeFragment).show(jokeFragment).hide(gameFragment).hide(aboutFragment).hide(mainFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                }
                break;
            case 3:
                // 趣图
                if (jokeImgFragment.isAdded()) {
                    ft.show(jokeImgFragment).hide(mainFragment).hide(gameFragment).hide(aboutFragment).hide(jokeFragment).hide(lollipopFragment).commit();
                } else {
                    ft.add(R.id.content_frame, jokeImgFragment).show(jokeImgFragment).hide(gameFragment).hide(aboutFragment).hide(jokeFragment).hide(mainFragment).hide(lollipopFragment).commit();
                }
                break;
            case 4:
                // android 5.0
                if (lollipopFragment.isAdded()) {
                    ft.show(lollipopFragment).hide(gameFragment).hide(mainFragment).hide(aboutFragment).hide(jokeFragment).hide(jokeImgFragment).commit();
                } else {
                    ft.add(R.id.content_frame, lollipopFragment).show(lollipopFragment).hide(gameFragment).hide(aboutFragment).hide(jokeFragment).hide(jokeImgFragment).hide(mainFragment).commit();
                }
                break;
            case 5:
                // 小游戏
                if (gameFragment.isAdded()) {
                    ft.show(gameFragment).hide(mainFragment).hide(aboutFragment).hide(jokeFragment).hide(jokeImgFragment).hide(lollipopFragment).commit();
                } else {
                    ft.add(R.id.content_frame, gameFragment).show(gameFragment).hide(aboutFragment).hide(jokeFragment).hide(jokeImgFragment).hide(mainFragment).hide(lollipopFragment).commit();
                }
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
//            openWebPage(getString(R.string.aboutme));
        } else if (item.getItemId() == R.id.menu_right) {
            openRightLayout();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    // 右边菜单开关事件

    public void openRightLayout() {

        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {

            mDrawerLayout.closeDrawer(Gravity.RIGHT);

        } else {

            mDrawerLayout.openDrawer(Gravity.RIGHT);

        }

    }
}
