package com.qingfengmy.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengmy.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/9.
 */
public class PaletteActivity extends BaseActivity {
    private static int TOTAL_COUNT = 13;

    @InjectView(R.id.pager_layout)
    RelativeLayout viewPagerContainer;
    @InjectView(R.id.view_pager)
    ViewPager viewPager;
    @InjectView(R.id.view_pager_index)
    TextView indexText;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @InjectView(R.id.Vibrant)
    TextView Vibrant;
    @InjectView(R.id.DarkVibrant)
    TextView DarkVibrant;
    @InjectView(R.id.LightVibrant)
    TextView LightVibrant;
    @InjectView(R.id.Muted)
    TextView Muted;
    @InjectView(R.id.DarkMuted)
    TextView DarkMuted;
    @InjectView(R.id.LightMuted)
    TextView LightMuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);

        ButterKnife.inject(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);

        viewPager.setAdapter(new MyPagerAdapter());
        // to cache all page, or we will see the right item delayed
        viewPager.setOffscreenPageLimit(TOTAL_COUNT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int viewpagerHight = dm.widthPixels * 9 / 16;
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(dm.widthPixels, viewpagerHight));

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
        myOnPageChangeListener.onPageSelected(0);
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

            ImageView imageView = new ImageView(PaletteActivity.this);
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

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.setting_bubble_1 + position);

            Palette.generateAsync(bmp, new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {

                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                    if (vibrant != null) {
                        Vibrant.setTextColor(vibrant.getTitleTextColor());
                        Vibrant.setBackgroundColor(vibrant.getRgb());

                        titleBar.setBackgroundColor(vibrant.getRgb());

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            // 5.0
                            Window window = getWindow();
                            window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                            window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                            // 4.4
                            SystemBarTintManager tintManager = new SystemBarTintManager(PaletteActivity.this);
                            tintManager.setStatusBarTintEnabled(true);
                            tintManager.setStatusBarTintColor(vibrant.getRgb());
                            tintManager.setNavigationBarTintColor(vibrant.getRgb());
                        }
                    }
                    Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                    if (darkVibrant != null) {
                        DarkVibrant.setTextColor(darkVibrant.getTitleTextColor());
                        DarkVibrant.setBackgroundColor(darkVibrant.getRgb());
                    }
                    Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                    if (lightVibrant != null) {
                        LightVibrant.setTextColor(lightVibrant.getTitleTextColor());
                        LightVibrant.setBackgroundColor(lightVibrant.getRgb());
                    }

                    Palette.Swatch muted = palette.getMutedSwatch();
                    if (muted != null) {
                        Muted.setTextColor(muted.getTitleTextColor());
                        Muted.setBackgroundColor(muted.getRgb());
                    }
                    Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                    if (darkMuted != null) {
                        DarkMuted.setTextColor(darkMuted.getTitleTextColor());
                        DarkMuted.setBackgroundColor(darkMuted.getRgb());
                    }
                    Palette.Swatch lightMuted = palette.getLightMutedSwatch();
                    if (lightMuted != null) {
                        LightMuted.setTextColor(lightMuted.getTitleTextColor());
                        LightMuted.setBackgroundColor(lightMuted.getRgb());
                    }


                }
            });
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

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }
}
