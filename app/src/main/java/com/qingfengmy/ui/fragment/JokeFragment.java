package com.qingfengmy.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.JokeAdapter;
import com.qingfengmy.ui.network.ApiClient;
import com.qingfengmy.ui.network.entities.Joke;
import com.qingfengmy.ui.network.entities.JokeResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeFragment extends Fragment {

    public static final int COUNT = 20;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Joke> jokeList;
    private JokeAdapter mAdapter;
    private int page;
    private ApiClient apiClient;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, null);
        ButterKnife.inject(this, view);

        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJokes();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        jokeList = new ArrayList<>();
        mAdapter = new JokeAdapter(getActivity(), jokeList);
        mRecyclerView.setAdapter(mAdapter);

        apiClient = new ApiClient();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new GetJokeTask().execute();
    }

    class GetJokeTask extends AsyncTask<Void, Void, List<Joke>> {

        @Override
        protected List<Joke> doInBackground(Void... params) {
            return new Select()
                    .from(Joke.class).where("image is null")
                    .execute();
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            if (jokes != null) {
                jokeList.addAll(jokes);
                mAdapter.notifyDataSetChanged();
            }
            mSwipeLayout.setRefreshing(true);
            getJokes();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetJokeTask().execute();
    }


    private void getJokes() {
        page = 1;
        apiClient.getJokeApi().getJokeListByNew(page, COUNT, new Callback<JokeResult>() {
            @Override
            public void success(JokeResult result, Response response) {
                mSwipeLayout.setRefreshing(false);
                if (result != null) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        jokeList.clear();
                        jokeList.addAll(result.getData());
                        mAdapter.notifyDataSetChanged();
                        // 存到数据库
                        for (int i = 0; i < jokeList.size(); i++) {
                            Joke joke = jokeList.get(i);
                            List<Joke> temJoke = new Select().from(Joke.class).where("jokeId ='" + joke.getJokeId() + "'").execute();
                            if (temJoke == null || temJoke.size() == 0)
                                joke.save();
                        }
                        Log.e("refresh aaa", "size = "+ jokeList.size());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

                mSwipeLayout.setRefreshing(false);
                Log.e("aaa", error.toString());
            }
        });
    }

    private void loadMore() {
        page++;
        apiClient.getJokeApi().getJokeListByNew(page, COUNT, new Callback<JokeResult>() {
            @Override
            public void success(JokeResult result, Response response) {
                if (result != null) {
                    jokeList.addAll(result.getData());
                    mAdapter.notifyDataSetChanged();
                    // 存到数据库
                    for (int i = 0; i < jokeList.size(); i++) {
                        Joke joke = jokeList.get(i);
                        List<Joke> temJoke = new Select().from(Joke.class).where("jokeId ='" + joke.getJokeId() + "'").execute();
                        if (temJoke == null || temJoke.size() == 0)
                            joke.save();
                    }
                    Log.e("aaa", "loadmore size = "+ jokeList.size());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("aaa", error.toString());
            }
        });
    }
}
