package com.qingfengmy.ui.network.services;

import com.qingfengmy.ui.network.entities.JokeList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/1/21.
 */
public interface JokesApi {

    /**
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     * key	string	是	您申请的key
     */
    @GET("/joke/content/text.from")
    void getJokeListByNew(
            @Query("page") int page,
            @Query("pagesize") int pagesize,
            @Query("key") String key,
            Callback<JokeList> callback);

    /**
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     * key	string	是	您申请的key
     */
    @GET("/joke/img/text.from")
    void getJokeImgListByNew(
            @Query("page") int page,
            @Query("pagesize") int pagesize,
            @Query("key") String key,
            Callback<JokeList> callback);
}
