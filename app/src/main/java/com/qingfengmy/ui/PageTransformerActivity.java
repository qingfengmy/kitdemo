package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.DepthPageTransformer;
import com.qingfengmy.ui.view.MyPageTransformer;
import com.qingfengmy.ui.view.RotateDownPageTransformer;
import com.qingfengmy.ui.view.ZoomOutPageTransformer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2015-01-05
 * Time: 14:22
 */
public class PageTransformerActivity extends BaseActivity {

    @InjectView(R.id.pager)
    ViewPager viewpager;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    private int[] mImgIds = new int[]{R.drawable.a,
            R.drawable.b, R.drawable.c, R.drawable.d};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagetransformer);
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

        initData();
        viewpager.setAdapter(new MyPagerAdapter());

        viewpager.setPageTransformer(true, new MyPageTransformer());
    }

    private void initData() {
        mImageViews.clear();
        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        initData();
        viewpager.setAdapter(new MyPagerAdapter());
        switch (item.getItemId()) {
            case R.id.menu_1:
                viewpager.setPageTransformer(true, new MyPageTransformer());
                break;
            case R.id.menu_2:
                viewpager.setPageTransformer(true, new DepthPageTransformer());
                break;
            case R.id.menu_3:
                viewpager.setPageTransformer(true, new ZoomOutPageTransformer());
                break;
            case R.id.menu_4:
                viewpager.setPageTransformer(true, new RotateDownPageTransformer());
                break;
            case R.id.menu_5:
                break;
        }
        return true;
    }

    public class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(mImageViews.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mImgIds.length;
        }
    }

}
