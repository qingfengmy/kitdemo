package com.qingfengmy.ui.network.services;

import com.qingfengmy.ui.network.entities.JokeResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/1/21.
 */
public interface JokesApi {

    /**
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     */
    @GET("/article/list/text")
    void getJokeListByNew(
            @Query("page") int page,
            @Query("count") int count,
            Callback<JokeResult> callback);

    /**
     * page	int	否	当前页数,默认1
     * pagesize	int	否	每次返回条数,默认1,最大20
     */
    @GET("/article/list/imgrank")
    void getJokeImgListByNew(
            @Query("page") int page,
            @Query("count") int count,
            Callback<JokeResult> callback);
}
