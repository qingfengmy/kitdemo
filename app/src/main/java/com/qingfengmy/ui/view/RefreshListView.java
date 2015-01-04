package com.qingfengmy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qingfengmy.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: zhangtao
 * Date: 2014-12-31
 * Time: 10:38
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    View header;// 顶部布局文件；
    int headerHeight;// 顶部布局文件的高度；
    int firstVisibleItem;// 当前第一个可见的item的位置；
    int scrollState;// listview 当前滚动状态；
    boolean isRemark;// 标记，当前是在listview最顶端摁下的；
    int startY;// 摁下时的Y值；

    int state;// 当前的状态；
    final int NONE = 0;// 正常状态；
    final int PULL = 1;// 提示下拉状态；
    final int RELESE = 2;// 提示释放状态；
    final int REFLASHING = 3;// 刷新状态；

    IReflashListener iReflashListener;//刷新数据的接口

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
    }

    /**
     * 初始化界面，添加顶部布局文件到 listview
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        header = inflater.inflate(R.layout.header_layout, null);
        // 设置为gone，上面还是会留一栏空行，故不可行。
//        header.setVisibility(View.GONE);
        // 测量header的高度
        measureHeadView(header);
        headerHeight = header.getMeasuredHeight();
        // 设置paddingtop为-headerHeight，这样headerview在初始时就显示不出来
        topPadding(-headerHeight);
        this.addHeaderView(header);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局，占用的宽，高；
     * listview的measureItem就是这么测量item的
     *
     * @param view
     */
    private void measureHeadView(View view) {
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
    }

    /**
     * 设置header 布局 上边距；
     *
     * @param topPadding
     */
    private void topPadding(int topPadding) {
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        // 通知重绘
        header.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
        if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
            // 停止滚动
            intercept = false;
        }else{
            intercept = true;
        }
        requestDisallowInterceptTouchEvent(intercept);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    boolean intercept;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHING;
                    // 加载最新数据；
                    reflashViewByState();
                    iReflashListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 判断移动过程操作；
     *
     * @param ev
     */
    private void moMove(MotionEvent ev) {
        if (!isRemark)
            return;
        int space = (int) (ev.getY() - startY);
        int topPadding = space - headerHeight;
        switch (state) {
            case NONE:
                if (space > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                // 更新head的位置，必须每次都更新，不能放到if中，否则header位置不变
                topPadding(topPadding);
                if (space > headerHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                // 更新head的位置
                topPadding(topPadding);
                if (space < headerHeight + 30) {
                    // 退回去，恢复pull状态
                    state = PULL;
                    reflashViewByState();
                } else if (space < 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            case REFLASHING:

                break;
        }
    }

    /**
     * 根据当前状态，改变界面显示；
     */
    public void reflashViewByState() {
        TextView tip = (TextView) header.findViewById(R.id.tip);
        ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
        ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);

        switch (state) {
            case NONE:
                arrow.clearAnimation();
                topPadding(-headerHeight);
                break;
            case PULL:
                tip.setText("下拉可以刷新");
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                break;
            case RELESE:
                tip.setText("松开可以刷新！");
                arrow.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                break;
            case REFLASHING:
                topPadding(headerHeight);
                arrow.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tip.setText("正在刷新...");
                break;
        }
    }

    /**
     * 获取完数据；
     */
    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
        TextView lastupdatetime = (TextView) header
                .findViewById(R.id.lastupdate_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        lastupdatetime.setText(time);
    }

    public void setInterface(IReflashListener iReflashListener) {
        this.iReflashListener = iReflashListener;
    }

    /**
     * 刷新数据接口
     *
     * @author Administrator
     */
    public interface IReflashListener {
        public void onReflash();
    }
}
