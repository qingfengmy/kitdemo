package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(titleBar);
        titleBar.setTitle(R.string.app_name);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> clazz;
        Intent intent = new Intent();
        try {
            clazz = Class.forName(classes[position]);
            intent.setClass(this, clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            showToast("不存在该类");
        }
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
}
