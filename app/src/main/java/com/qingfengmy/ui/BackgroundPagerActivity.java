package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.test.PerformanceTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.ParallaxViewPager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2015-01-06
 * Time: 18:11
 */
public class BackgroundPagerActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.pager)
    ParallaxViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backgroundpager);
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
        pager.setBackgroundResource(R.drawable.wide_bg);
        pager.setAdapter(new MyPagerAdapter());

    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView textView = new TextView(BackgroundPagerActivity.this);
            textView.setText("好酒不长有");
            textView.setTextSize(30);
            ((ViewPager) container).addView(textView, position);
            return textView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((TextView) object);
        }
    }
}
