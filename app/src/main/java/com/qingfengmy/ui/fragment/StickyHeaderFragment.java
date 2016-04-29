package com.qingfengmy.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.StickyHeaderActivity;
import com.qingfengmy.ui.adapters.ViewPagerAdapter;
import com.qingfengmy.ui.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

/**
 * Created by Administrator on 2015/1/26.
 */
public class StickyHeaderFragment extends Fragment {

    @InjectView(R.id.mlistview)
    LoadMoreListView mListView;
    @InjectView(R.id.textView)
    TextView textView;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    @InjectView(R.id.viewpager_doslayout)
    LinearLayout dots_layout;

    // 圆圈的list
    List<View> dots = new ArrayList<View>();

    StickyHeaderActivity activity;


    public StickyHeaderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stickyheader, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (StickyHeaderActivity) getActivity();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int viewpagerHight = dm.widthPixels * 9 / 16;
        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(dm.widthPixels, viewpagerHight));

        viewPager.setAdapter(new ViewPagerAdapter(getActivity()));
        // 初始化dots
        for (int i = 0; i < 4; i++) {
            View child = new View(getActivity());
            // dot的大小
            int do_len = getResources().getDimensionPixelSize(R.dimen.do_len);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(do_len, do_len);
            params.setMargins(5, 0, 5, 0);
            child.setLayoutParams(params);
            if (i == 0)
                child.setBackgroundResource(R.drawable.dot_focused);
            else
                child.setBackgroundResource(R.drawable.dot_normal);
            dots.add(child);
            dots_layout.addView(child);
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 设置圆点位置
                for (int i = 0; i < dots.size(); i++) {
                    if (i == position)
                        dots.get(position).setBackgroundResource(
                                R.drawable.dot_focused);
                    else
                        dots.get(i)
                                .setBackgroundResource(R.drawable.dot_normal);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == ViewPager.SCROLL_STATE_IDLE) {
//                    mPtrFrame.requestDisallowInterceptTouchEvent(false);
//                } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    mPtrFrame.requestDisallowInterceptTouchEvent(true);
//                }
                Log.e("aaa", "state="+state);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int height = textView.getHeight();
        StikkyHeaderBuilder.stickTo(mListView)
                .setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeaderRes(R.dimen.min_height_header)
//            .animator(new ParallaxStikkyAnimator())
                .build();

        populateListView();

    }

    private class InitializeApplicationsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mListView.setCanLoadMore(true);
        }
    }

    private class ParallaxStikkyAnimator extends HeaderStikkyAnimator {

        @Override
        public AnimatorBuilder getAnimatorBuilder() {
            View mHeader_image = getHeader().findViewById(R.id.header_image);

            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

    LinkedList<String> elements;
    ArrayAdapter adapter;

    private void populateListView() {

        elements = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            elements.add("row---" + i);
        }

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, elements);
        mListView.setAdapter(adapter);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadMoreTask().execute();
            }
        });
    }

    private class LoadMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            elements.addLast("recyclerview's footview--");
            adapter.notifyDataSetChanged();
            mListView.onLoadMoreComplete();
            mListView.setCanLoadMore(true);
        }
    }
}
