package com.qingfengmy.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.entity.AppInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * 所有activity的父类
 *
 * @author zhangtao(qingfengmy@126.com)
 *         <p/>
 *         2014-3-15
 */
public class BaseActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        setSwipeBackEnable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        // 设置一个颜色给系统栏(系统栏包括导航栏-魅族的底栏和状态栏)
//        tintManager.setTintColor(getResources().getColor(R.color.primary));
        // 设置一个样式背景给导航栏
        tintManager.setNavigationBarTintColor(getResources().getColor(R.color.primary_dark));
        // 设置一个状态栏资源
        tintManager.setStatusBarTintColor(getResources().getColor(R.color.primary));
    }

    public String getName(Activity activity) {
        return activity.getClass().getSimpleName();
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void animateActivity(AppInfo appInfo, View appIcon) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("appInfo", appInfo.getComponentName());

        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(appIcon, "appIcon"));
        startActivity(i, transitionActivityOptions.toBundle());
    }

    public void openWebPage(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }

}
