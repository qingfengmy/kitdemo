package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-29
 * Time: 16:59
 */
public class ViewPagerMultiActivity extends BaseActivity {
    private static int TOTAL_COUNT = 14;

    @InjectView(R.id.pager_layout)
    RelativeLayout viewPagerContainer;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.view_pager_index)
    TextView indexText;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagermulti);
        ButterKnife.inject(this);

        setSupportActionBar(titleBar);
        titleBar.setTitle(names[8]);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);


        viewPager.setAdapter(new MyPagerAdapter());
        // to cache all page, or we will see the right item delayed
        viewPager.setOffscreenPageLimit(TOTAL_COUNT);
        viewPager.setPageMargin(getResources().getDimensionPixelSize(
                R.dimen.pager_margin));
        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
        viewPager.setOnPageChangeListener(myOnPageChangeListener);

        viewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem
                // that we can swipe only the middle view.
                return viewPager.dispatchTouchEvent(event);
            }
        });
        indexText.setText(new StringBuilder().append("1/").append(TOTAL_COUNT));

    }

    /**
     * this is a example fragment, just a imageview, u can replace it with your
     * needs
     *
     * @author Trinea 2013-04-03
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return TOTAL_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(ViewPagerMultiActivity.this);
            imageView.setImageResource(R.drawable.setting_bubble_1 + position);
            ((ViewPager) container).addView(imageView, position);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            indexText.setText(new StringBuilder().append(position + 1)
                    .append("/").append(TOTAL_COUNT));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            // to refresh frameLayout
            if (viewPagerContainer != null) {
                viewPagerContainer.invalidate();
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
