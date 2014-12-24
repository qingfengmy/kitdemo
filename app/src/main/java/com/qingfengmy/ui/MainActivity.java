package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
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
    private ArrayAdapter<String> adapter;

    private String[] names;
    private String[] classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        names = getResources().getStringArray(R.array.names);
        classes = getResources().getStringArray(R.array.classes);

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
}
