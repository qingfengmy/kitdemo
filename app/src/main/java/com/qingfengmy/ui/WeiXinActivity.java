package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.Tab1Fragment;
import com.qingfengmy.ui.fragment.Tab2Fragment;
import com.qingfengmy.ui.fragment.Tab3Fragment;
import com.qingfengmy.ui.fragment.TabFragment;
import com.qingfengmy.ui.view.ChangeColorIconWithTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/20.
 */
public class WeiXinActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener, View.OnClickListener {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.id_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.id_indicator_one)
    ChangeColorIconWithTextView one;
    @InjectView(R.id.id_indicator_two)
    ChangeColorIconWithTextView two;
    @InjectView(R.id.id_indicator_three)
    ChangeColorIconWithTextView three;

    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private String[] mTitles = new String[]{"First Fragment!",
            "Second Fragment!", "Third Fragment!"};

    private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin);
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

        initDatas();

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void initDatas() {

//        for (String title : mTitles) {
//            TabFragment tabFragment = new TabFragment();
//            Bundle args = new Bundle();
//            args.putString("title", title);
//            tabFragment.setArguments(args);
//        }
        TabFragment fragment = new TabFragment();
        mTabs.add(fragment);
        Tab1Fragment fragment1 = new Tab1Fragment();
        mTabs.add(fragment1);
        Tab2Fragment fragment2 = new Tab2Fragment();
        mTabs.add(fragment2);
        Tab3Fragment fragment3 = new Tab3Fragment();
        mTabs.add(fragment3);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mTabs.get(arg0);
            }
        };

        initTabIndicator();

    }

    private void initTabIndicator() {
        mTabIndicator.add(one);
        mTabIndicator.add(two);
        mTabIndicator.add(three);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }

    @Override
    public void onPageSelected(int arg0) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // Log.e("TAG", "position = " + position + " , positionOffset = "
        // + positionOffset);

        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIndicator.get(position);
            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicator.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
        }

    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

}
