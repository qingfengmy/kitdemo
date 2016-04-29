package com.qingfengmy.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.AboutFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/6/25.
 */
public class TabLayoutActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.tablayout)
    TabLayout tabLayout;
    @InjectView(R.id.vp)
    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);
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

        tabLayout.setTabTextColors(Color.WHITE, Color.YELLOW);//设置文本在选中和为选中时候的颜色
        tabLayout.addTab(tabLayout.newTab().setText("第一个"), true);//添加 Tab,默认选中
        tabLayout.addTab(tabLayout.newTab().setText("第二个"), false);//添加 Tab,默认不选中
        tabLayout.addTab(tabLayout.newTab().setText("第三个"), false);//添加 Tab,默认不选中
        tabLayout.addTab(tabLayout.newTab().setText("第4个"), false);//添加 Tab,默认不选中
//        tabLayout.addTab(tabLayout.newTab().setText("第5个"), false);//添加 Tab,默认不选中
//        tabLayout.addTab(tabLayout.newTab().setText("第6个"), false);//添加 Tab,默认不选中

//        ArrayList<TextView> tvs = new ArrayList<TextView>();
//        for (int i = 0; i < items.length; i++) {
//            TextView tv = new TextView(this);
//            tv.setText(items[i]);
//            LinearLayout.LayoutParams lp =
//                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            tv.setTextColor(Color.BLACK);
//            tv.setBackgroundColor(Color.WHITE);
//            tv.setGravity(Gravity.CENTER);
//            tv.setLayoutParams(lp);
//            tv.setTextSize(22);
//            tvs.add(tv);
//        }
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AboutFragment(), "第一个");
        adapter.addFragment(new AboutFragment(), "第二个");
        adapter.addFragment(new AboutFragment(), "第三个");
        adapter.addFragment(new AboutFragment(), "第四个");
//        adapter.addFragment(new AboutFragment(), "第五个");
//        adapter.addFragment(new AboutFragment(), "第六个");
        vp.setAdapter(adapter);

        //用来设置tab的，同时也要覆写  PagerAdapter 的 CharSequence getPageTitle(int position) 方法，要不然 Tab 没有 title
        tabLayout.setupWithViewPager(vp);
        //关联 TabLayout viewpager
        tabLayout.setTabsFromPagerAdapter(adapter);
        // 模式可以设置为滚动或者固定等
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
