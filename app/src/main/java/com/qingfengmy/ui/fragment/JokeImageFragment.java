package com.qingfengmy.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.activeandroid.query.Select;
import com.qingfengmy.R;
import com.qingfengmy.ui.PhotoActivity;
import com.qingfengmy.ui.adapters.JokeImgAdapter;
import com.qingfengmy.ui.network.ApiClient;
import com.qingfengmy.ui.network.entities.Joke;
import com.qingfengmy.ui.network.entities.JokeResult;
import com.qingfengmy.ui.view.EmptyView;
import com.qingfengmy.ui.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeImageFragment extends Fragment {

    public static final String TYPE = "type";
    @InjectView(R.id.list_view_with_empty_view_fragment_ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    List<Joke> jokeList;
    JokeImgAdapter jokeAdapter;
    @InjectView(R.id.empty_view)
    EmptyView emptyView;
    int page;
    ApiClient apiClient;
    boolean canLoadMore;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke_img, null);
        ButterKnife.inject(this, view);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getJokeImgs();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 第二个参数必须是listview，否则滑动会出错
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }
        });

        // the following are default_image settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default_image is false
        mPtrFrame.setPullToRefresh(false);
        // default_image is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore)
                    loadMore();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String img = jokeList.get(position).getImage();
                Intent intent = new Intent(getActivity(), PhotoActivity.class);
                intent.putExtra("img", img);
                startActivity(intent);
            }
        });
        listView.setEmptyView(emptyView);
        jokeList = new ArrayList<Joke>();
        apiClient = new ApiClient();

        return view;
    }

    class GetJokeTask extends AsyncTask<Void, Void, List<Joke>> {

        @Override
        protected List<Joke> doInBackground(Void... params) {
            return new Select().from(Joke.class).where("image is not null").execute();
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            jokeList.addAll(jokes);
            jokeAdapter = new JokeImgAdapter(getActivity(), jokeList);
            listView.setAdapter(jokeAdapter);

            mPtrFrame.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPtrFrame.autoRefresh();
                }
            }, 100);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new GetJokeTask().execute();
    }

    private void getJokeImgs() {
        page = 1;
        apiClient.getJokeApi().getJokeImgListByNew(page, 20, new Callback<JokeResult>() {
            @Override
            public void success(JokeResult result, Response response) {
                mPtrFrame.refreshComplete();
                if (result != null) {
                    if (result.getData() != null && result.getData().size() > 0) {
                        jokeList.clear();
                        jokeList.addAll(result.getData());
                        jokeAdapter = new JokeImgAdapter(getActivity(), jokeList);
                        listView.setAdapter(jokeAdapter);

                        // 存到数据库
                        for (int i = 0; i < jokeList.size(); i++) {
                            Joke joke = jokeList.get(i);
                            List<Joke> temJoke = new Select().from(Joke.class).where("jokeId ='" + joke.getJokeId() + "'").execute();
                            if (temJoke == null || temJoke.size() == 0)
                                joke.save();
                        }
                    }
                    canLoadMore = jokeList.size() < result.getTotal();
                    listView.setCanLoadMore(canLoadMore);
                } else {
                    Log.e("aaa", "jokelist is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("aaa", error.toString());
                mPtrFrame.refreshComplete();
            }
        });
    }

    private void loadMore() {
        page++;
        apiClient.getJokeApi().getJokeImgListByNew(page, 20, new Callback<JokeResult>() {
            @Override
            public void success(JokeResult result, Response response) {
                listView.onLoadMoreComplete();
                if (result != null) {
                    jokeList.addAll(result.getData());
                    jokeAdapter.notifyDataSetChanged();

                    // 存到数据库
                    for (int i = 0; i < jokeList.size(); i++) {
                        Joke joke = jokeList.get(i);
                        List<Joke> temJoke = new Select().from(Joke.class).where("jokeId ='" + joke.getJokeId() + "'").execute();
                        if (temJoke == null || temJoke.size() == 0)
                            joke.save();
                    }
                    canLoadMore = jokeList.size() < result.getTotal();
                    listView.setCanLoadMore(canLoadMore);
                } else {
                    Log.e("aaa", "jokelist is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                listView.onLoadMoreComplete();
                Log.e("aaa", error.toString());
            }
        });
    }

}
