package com.qingfengmy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.qingfengmy.R;
import com.qingfengmy.ui.utils.tools.ToastUtil;
import com.qingfengmy.ui.view.ViewPagerFixed;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2015/3/26.
 */
public class PhotoActivity extends BaseActivity{

    private static int TOTAL_COUNT;

    @InjectView(R.id.big_pager_layout)
    RelativeLayout bigviewpagerLayout;

    @InjectView(R.id.big_view_pager)
    ViewPagerFixed bigviewpager;
    @InjectView(R.id.big_view_pager_index)
    TextView bigindexText;

    List<String> images;
    int currentPosition;

    private ImageLoader loader;
    private DisplayImageOptions options;
    LayoutInflater mInflater;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.inject(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        images = new ArrayList<>();
        images.add("http://ww1.sinaimg.cn/bmiddle/9e5389bbjw1eqj2c569myj20go0b4dgx.jpg");
        images.add("http://ww1.sinaimg.cn/bmiddle/9e5389bbjw1eqj2bwpvn2j20go09egmx.jpg");
        images.add("http://ww3.sinaimg.cn/bmiddle/9e5389bbjw1eqj2byyy71j20go0b475c.jpg");
        images.add("http://ww3.sinaimg.cn/bmiddle/9e5389bbjw1eqj2c0rtllj20b40gojt6.jpg");
        images.add("http://ww2.sinaimg.cn/bmiddle/9e5389bbjw1eqj2c2qsvhj20go0b4q4o.jpg");
        images.add("http://ww2.sinaimg.cn/bmiddle/9e5389bbjw1eqj2c8y2plj20go0b4aaz.jpg");
        currentPosition = intent.getIntExtra("position", -1);

        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnLoading(R.drawable.default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        TOTAL_COUNT = images.size();

        bigviewpager.setAdapter(new MyPagerAdapter(images));
        bigviewpager.setOffscreenPageLimit(TOTAL_COUNT);

        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
        bigviewpager.setOnPageChangeListener(myOnPageChangeListener);
        bigindexText.setText(new StringBuilder().append("1/").append(TOTAL_COUNT));
        bigviewpager.setCurrentItem(currentPosition);
    }


    class MyPagerAdapter extends PagerAdapter {

        private List<String> images;

        public MyPagerAdapter(List<String> images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View convertView = mInflater.inflate(R.layout.dialog_image,
                    null);
            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);
            loader.displayImage(images.get(position), imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    ToastUtil.showToast(PhotoActivity.this, "图片加载失败");
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    PhotoViewAttacher mAttacher = new PhotoViewAttacher((ImageView)view);
                    mAttacher.update();
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            container.addView(convertView);
            return convertView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            bigindexText.setText(new StringBuilder().append(position + 1)
                    .append("/").append(TOTAL_COUNT));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
