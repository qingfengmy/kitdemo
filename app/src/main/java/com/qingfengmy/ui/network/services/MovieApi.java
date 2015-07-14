package com.qingfengmy.ui.network.services;

import com.qingfengmy.ui.network.entities.MovieResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Administrator on 2015/7/14.
 */
public interface MovieApi {

    @GET("/movie/search")
    void getMovies(
            @Query("tag") String tag,
            @Query("start") int start,
            @Query("end") int end,
            Callback<MovieResult> callback);
}
