package com.qingfengmy.ui.adapters.pager;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.qingfengmy.ui.adapters.JokeAdapter;
import com.qingfengmy.ui.network.ApiClient;
import com.qingfengmy.ui.network.entities.Joke;
import com.qingfengmy.ui.network.entities.JokeList;
import com.qingfengmy.ui.utils.Constants;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeImgPager extends EndlessAdapter {

    ApiClient apiClient;
    int page;
    List<Joke> jokes;

    public JokeImgPager(Context context, ListAdapter wrapped, int pendingResource) {
        super(context, wrapped, pendingResource);
        apiClient = new ApiClient();
        page = 2;
    }

    @Override
    protected boolean cacheInBackground() throws Exception {
        getJokes();

        // 返回false则请求结束，没有更多数据时才返回false
        return true;
    }

    @Override
    protected void appendCachedData() {
        if (jokes != null) {
            JokeAdapter jokeAdapter = (JokeAdapter) getWrappedAdapter();
            jokeAdapter.addJokes(jokes);
        }
    }

    private void getJokes() {
        apiClient.getJokeApi().getJokeImgListByNew(page, 20, Constants.OpenID, new Callback<JokeList>() {
            @Override
            public void success(JokeList list, Response response) {
                if (list != null) {
                    if (list.getResult() != null) {
                        if (list.getResult().getJokeList() != null) {
                            jokes = list.getResult().getJokeList();
                            page++;
                        } else {
                            Log.e("aaa", "jokelist is null");
                        }
                    } else {
                        Log.e("aaa", "result is null");
                    }
                } else {
                    Log.e("aaa", "list is null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("aaa", error.toString());
            }
        });
    }
}
