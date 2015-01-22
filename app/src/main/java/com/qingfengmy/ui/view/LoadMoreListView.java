package com.qingfengmy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

import com.qingfengmy.R;

/**
 * Created by Administrator on 2015/1/19.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {


    int totalItemCount;// adapter的总数
    int lastVisibleItem;// 最后可见位置，而且listView是从0开始数的。
    boolean isLoading;// 正在加载
    private Context context;
    private LayoutInflater mInflater;
    private View footView;
    private LoadMoreListenner loadMoreListenner;

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        footView = mInflater.inflate(R.layout.pending, null);
        addFooterView(footView);

        hideFootView();

        setOnScrollListener(this);
    }

    private void showFootView() {
        addFooterView(footView);
    }

    private void hideFootView() {
        removeFooterView(footView);
    }

    public void loadMoreComplete() {
        isLoading = false;
        hideFootView();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e("aaa", "scrollState="+scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibleItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
        Adapter mAdapter = getAdapter();
        if (mAdapter != null) {
            // lastVisibleItem是最后的可见位置，其实是firstVisibleItem + visibleItemCount - 1；
            // lastVisibleItem是从0开始数的，totalItemCount是从1开始数的，所以应该是totalItemCount==totalItemCount-1
            // 因为上面两个变量都减了1，所以这里直接写totalItemCount == lastVisibleItem
            if (totalItemCount == lastVisibleItem) {
                if (!isLoading) {
                    isLoading = true;
                    showFootView();
                    loadMoreListenner.loadMore();
                    Log.e("aaa", "showFootView");
                }
            }
        } else {
            Log.e("aaa", "adapter is null");
//            throw new IllegalStateException("the listView has no adapter");
        }
        Log.e("aaa", "lastVisibleItem="+lastVisibleItem+",totalItemCount="+totalItemCount);
    }

    public void setLoadMoreListenner(LoadMoreListenner loadMoreListenner) {
        this.loadMoreListenner = loadMoreListenner;
    }

    public interface LoadMoreListenner {
        public void loadMore();
    }

}
