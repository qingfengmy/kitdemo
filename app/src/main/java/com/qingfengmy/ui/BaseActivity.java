package com.qingfengmy.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.entity.AppInfo;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 所有activity的父类
 *
 * @author zhangtao(qingfengmy@126.com)
 *         <p/>
 *         2014-3-15
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            window.setNavigationBarColor(getResources().getColor(R.color.primary));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            // 4.4
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.primary_dark));
            tintManager.setNavigationBarTintColor(getResources().getColor(R.color.primary));
        }
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
