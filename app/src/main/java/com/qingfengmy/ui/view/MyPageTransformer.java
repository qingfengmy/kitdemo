package com.qingfengmy.ui.view;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import android.support.v4.view.ViewPager.*;

import com.nineoldandroids.view.ViewHelper;

/**
 * User: zhangtao
 * Date: 2015-01-05
 * Time: 14:34
 * <p/>
 * 要理解transformPage(View view, float position)的参数。
 * view理所当然就是滑动中的那个view，position这里是float类型，
 * 不是平时理解的int位置，而是当前滑动状态的一个表示，
 * 比如当滑动到正全屏时，position是0，而向左滑动，
 * 使得右边刚好有一部被进入屏幕时，position是1，
 * 如果前一页和下一页基本各在屏幕占一半时，
 * 前一页的position是-0.5，后一页的posiotn是0.5
 * viewpager默认是留3页数据的，即前一页，当前页，后一页，
 * 也就是transformPage会执行三次，position是每一页的最边在屏幕的比例（0-1）
 */
public class MyPageTransformer implements PageTransformer {

    private static final float MAX_SCALE = 0.8f;

    @Override
    public void transformPage(View page, float position) {

        int width = page.getMeasuredWidth();
        int height = page.getMeasuredHeight();
        int w = page.getWidth();
        int h = page.getHeight();
        Log.e("aa", "width=" + width + ",height=" + height + ",w=" + w + ",h=" + h);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setScaleX(page, 1);
            ViewHelper.setScaleY(page, 1);
        } else if (position <= 0) { // [-1,0]
            // Use the default_image slide transition when moving to the left page
            // 设置中心点在左边
            ViewHelper.setPivotX(page, 0);
            ViewHelper.setPivotY(page, h / 2);
            // 计算梯度值
            float scale = 1 + (1 - MAX_SCALE) * position;
            // 缩放动画
            ViewHelper.setScaleX(page, scale);
            ViewHelper.setScaleY(page, scale);
        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            // 设置中心点右边
            ViewHelper.setPivotX(page, w);
            ViewHelper.setPivotY(page, h / 2);
            // 计算梯度值
            float scale = 1 - (1 - MAX_SCALE) * position;
            // 缩放动画
            ViewHelper.setScaleX(page, scale);
            ViewHelper.setScaleY(page, scale);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setScaleX(page, 1);
            ViewHelper.setScaleY(page, 1);
        }
    }
}
