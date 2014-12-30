package com.qingfengmy.ui.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.qingfengmy.R;

/**
 * User: zhangtao
 * Date: 2014-12-30
 * Time: 09:55
 */
public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;

    private int mMenuWidth;
    // dp
    private int mMenuRightPadding;

    private boolean once;

    private boolean isOpen;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu);
        mMenuRightPadding = (int) a.getDimension(R.styleable.SlidingMenu_menuWidth, 100);
        a.recycle();

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
    }

    /**
     * 设置子view的宽高和自己的宽高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            // 设置menu的宽度
            mMenuWidth = mMenu.getLayoutParams().width = mMenuRightPadding;
            // 设置mContent的宽度
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     * 通过设置偏移量，将menu隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                // 判断滑动距离
                int scrollX = getScrollX();
                if (scrollX > mMenuWidth / 2) {
                    // 滚动距离大于menu的一半，缩回去（滚动距离是指滚动条距离左端的距离）
                    smoothScrollTo(mMenuWidth, 0);
                } else {
                    smoothScrollTo(0, 0);
                }
                return true;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 抽屉式的侧滑，需要在滚动时，改变menu的位置
     * 滚动0，menu偏移mMenuWidth
     * 滚动100,表面是左偏移100，其实是向右（x轴正方向）移动mMenuWidth-100，依次保证表面偏移100
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // 动画用到的梯度值
        float scale = l*1.0f/mMenuWidth;
        // 内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 (0.7 + 0.3 * scale)
        float rightScale = 0.7f + 0.3f * scale;
        // menu的缩放
        float leftMove = 0.7f*scale;
        // 菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 (1.0 - scale * 0.3)
        float leftScale = 1.0f - scale * 0.3f;
        //  透明度 0.6 ~ 1.0  (0.6+ 0.4 * (1- scale))
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // 属性动画
//        ObjectAnimator animator = ObjectAnimator.ofFloat(mMenu, "translationX", 0, mMenuWidth*scale);
//        animator.setDuration(0);
//        animator.start();

        // QQ的侧滑效果，menu有缩放的效果和透明度的变化，content也有缩放的效果
        // 设置content的缩放中心点
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
        // 设置content的缩放动画
        ViewHelper.setScaleX(mContent,rightScale);
        ViewHelper.setScaleY(mContent,rightScale);
        // 设置menu的缩放和透明度
        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);
        // 设置menu的偏移（nineold动画库可以兼容3.0以下的设备实现属性动画，而且使用方便）
        ViewHelper.setTranslationX(mMenu, leftMove);
    }
}
