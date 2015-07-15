package com.qingfengmy.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2015/7/15.
 */
public class DragViewLayout extends LinearLayout {
    private ViewDragHelper mDragger;
    // 演示简单的移动
    private View mDragView;
    // 演示除了移动后，松手自动返回到原本的位置。（注意你拖动的越快，返回的越快）
    private View mAutoBackView;
    // 边界移动时对View进行捕获。
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    public DragViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 创建实例需要3个参数，第一个就是当前的ViewGroup，第二个sensitivity，主要用于设置touchSlop:

         helper.mTouchSlop = (int) (helper.mTouchSlop * (1 / sensitivity));

         可见传入越大，mTouchSlop的值就会越小。第三个参数就是Callback，在用户的触摸过程中会回调相关方法，后面会细说。
         */
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                // 如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
                //mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView;
            }

            /**
             * clampViewPositionHorizontal,clampViewPositionVertical可以在该方法中对child移动的边界进行控制，
             * left , top 分别为即将移动到的位置，
             * 比如横向的情况下，我希望只在ViewGroup的内部移动，即：
             * 最小>=paddingleft，最大<=ViewGroup.getWidth()-paddingright-child.getWidth。
             */

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel)
            {
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView)
                {
                    // 回到初始的位置
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }

            //在边界拖动时回调
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId)
            {
                mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }

            /** 如果你用Button测试，或者给TextView添加了clickable = true ，都记得重写下面这两个方法
             * 主要是因为，如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent，
             * 在onTouchEvent的DOWN的时候就确定了captureView。如果消耗事件，
             * 那么就会先走onInterceptTouchEvent方法，判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法：
             * getViewHorizontalDragRange和getViewVerticalDragRange，只有这两个方法返回大于0的值才能正常的捕获。
            */
            @Override
            public int getViewHorizontalDragRange(View child)
            {
                return getMeasuredWidth()-child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child)
            {
                return getMeasuredHeight()-child.getMeasuredHeight();
            }
        });

        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll()
    {
        if(mDragger.continueSettling(true))
        {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        // 保存了最开启的位置信息
        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDragView = getChildAt(1);
        mAutoBackView = getChildAt(2);
        mEdgeTrackerView = getChildAt(3);
    }


}
