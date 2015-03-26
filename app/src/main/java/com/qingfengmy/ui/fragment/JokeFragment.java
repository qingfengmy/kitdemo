package com.qingfengmy.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.commonsware.cwac.endless.EndlessAdapter;
import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.JokeAdapter;
import com.qingfengmy.ui.adapters.pager.JokeImgPager;
import com.qingfengmy.ui.adapters.pager.JokePager;
import com.qingfengmy.ui.network.ApiClient;
import com.qingfengmy.ui.network.entities.Joke;
import com.qingfengmy.ui.network.entities.JokeList;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.view.EmptyView;

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
public class JokeFragment extends Fragment {

    public static final String TYPE = "type";
    @InjectView(R.id.list_view_with_empty_view_fragment_ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @InjectView(R.id.listView)
    ListView listView;
    List<Joke> jokeList;
    JokeAdapter jokeAdapter;
    @InjectView(R.id.empty_view)
    EmptyView emptyView;
    int page;
    ApiClient apiClient;
    EndlessAdapter jokePager;

    private boolean isjoke;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joke, null);
        ButterKnife.inject(this, view);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                if (isjoke)
                    getJokes();
                else
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

        listView.setEmptyView(emptyView);
        jokeList = new ArrayList<Joke>();
        apiClient = new ApiClient();

        return view;
    }

    class GetJokeTask extends AsyncTask<Void, Void, List<Joke>> {

        @Override
        protected List<Joke> doInBackground(Void... params) {
            if (isjoke)
                return new Select()
                        .from(Joke.class).where("url is null")
                        .execute();
            else
                return new Select().from(Joke.class).where("url is not null").execute();
        }

        @Override
        protected void onPostExecute(List<Joke> jokes) {
            jokeList.addAll(jokes);
            jokeAdapter = new JokeAdapter(getActivity(), jokeList);
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

        isjoke = getArguments().getBoolean(TYPE);
        new GetJokeTask().execute();
    }

    private void getJokeImgs() {
        apiClient.getJokeApi().getJokeImgListByNew(page, 20, Constants.OpenID, new Callback<JokeList>() {
            @Override
            public void success(JokeList list, Response response) {
                if (list != null) {
                    if (list.getResult() != null) {
                        if (list.getResult().getJokeList() != null) {
                            jokeList = list.getResult().getJokeList();
                            jokeAdapter = new JokeAdapter(getActivity(), jokeList);
                            jokePager = new JokeImgPager(getActivity(), jokeAdapter, R.layout.pending);
                            listView.setAdapter(jokePager);

                            // 存到数据库
                            for (int i = 0; i < jokeList.size(); i++) {
                                Joke joke = jokeList.get(i);
                                List<Joke> temJoke = new Select().from(Joke.class).where("hashId ='" + joke.getHashId()+"'").execute();
                                if (temJoke == null || temJoke.size() == 0)
                                    joke.save();
                            }
                        } else {
                            Log.e("aaa", "jokelist is null");
                        }
                    } else {
                        Log.e("aaa", "result is null");
                    }
                } else {
                    Log.e("aaa", "list is null");
                }
                mPtrFrame.refreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("aaa", error.toString());
                mPtrFrame.refreshComplete();
            }
        });
    }

    private void getJokes() {
        apiClient.getJokeApi().getJokeListByNew(page, 20, Constants.OpenID, new Callback<JokeList>() {
            @Override
            public void success(JokeList list, Response response) {
                if (list != null) {
                    if (list.getResult() != null) {
                        if (list.getResult().getJokeList() != null) {
                            jokeList = list.getResult().getJokeList();
                            jokeAdapter = new JokeAdapter(getActivity(), jokeList);
                            jokePager = new JokePager(getActivity(), jokeAdapter, R.layout.pending);
                            listView.setAdapter(jokePager);

                            // 存到数据库
                            for (int i = 0; i < jokeList.size(); i++) {
                                Joke joke = jokeList.get(i);
                                List<Joke> temJoke = new Select().from(Joke.class).where("hashId ='" + joke.getHashId()+"'").execute();
                                if (temJoke == null || temJoke.size() == 0)
                                    joke.save();
                            }
                        } else {
                            Log.e("aaa", "jokelist is null");
                        }
                    } else {
                        Log.e("aaa", "result is null");
                    }
                } else {
                    Log.e("aaa", "list is null");
                }
                mPtrFrame.refreshComplete();
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("aaa", error.toString());
                mPtrFrame.refreshComplete();
            }
        });
    }
}
