package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingfengmy.R;

/**
 * User: zhangtao
 * Date: 2014-11-21
 * Time: 14:58
 */
public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mInflater;
    int[] imgs = new int[]{
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d
    };

    public ViewPagerAdapter(Context mContext) {
        this.context = mContext;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = (ImageView) mInflater.inflate(R.layout.img_viewpager,null);
        view.setImageResource(imgs[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
