package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.CommonAdapter;
import com.qingfengmy.ui.entity.Bean;
import com.qingfengmy.ui.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/5/18.
 */
public class CommonAdapterActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.listView)
    ListView listView;

    List<Bean> beans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_adapter);

        ButterKnife.inject(this);
        mToolbar.setTitle(getName(this));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initDatas();

        CommonAdapter<Bean> mAdapter = new CommonAdapter<Bean>(
                getApplicationContext(), beans, R.layout.item_common_adapter) {
            @Override
            public void convert(ViewHolder helper, Bean item) {
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_describe, item.getDesc());
                helper.setText(R.id.tv_phone, item.getPhone());
                helper.setText(R.id.tv_time, item.getTime());
            }

        };

        listView.setAdapter(mAdapter);

    }

    private void initDatas() {

        beans = new ArrayList<>();
        Bean bean1 = new Bean("张三", "捡到一个钱包", "星期三", "110");
        beans.add(bean1);
        Bean bean2 = new Bean("张四", "捡到二个钱包", "星期三", "110");
        beans.add(bean2);
        Bean bean3 = new Bean("张五", "捡到三个钱包", "星期三", "110");
        beans.add(bean3);
        Bean bean4 = new Bean("张六", "捡到四个钱包", "星期三", "110");
        beans.add(bean4);
        Bean bean5 = new Bean("张七", "捡到五个钱包", "星期三", "110");
        beans.add(bean5);
        Bean bean6 = new Bean("张八", "捡到六个钱包", "星期三", "110");
        beans.add(bean6);
        Bean bean7 = new Bean("张九", "捡到七个钱包", "星期三", "110");
        beans.add(bean7);
        Bean bean8 = new Bean("张十", "捡到八个钱包", "星期三", "110");
        beans.add(bean8);
        Bean bean9 = new Bean("张十一", "捡到九个钱包", "星期三", "110");
        beans.add(bean9);
        Bean bean10 = new Bean("张十二", "捡到十个钱包", "星期三", "110");
        beans.add(bean10);

    }


}
