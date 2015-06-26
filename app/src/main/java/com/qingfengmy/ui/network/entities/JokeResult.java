package com.qingfengmy.ui.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeResult {

    @SerializedName("count")
    private int count;

    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    @SerializedName("err")
    private int err;
    @SerializedName("items")
    private List<Joke> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public List<Joke> getData() {
        return data;
    }

    public void setData(List<Joke> data) {
        this.data = data;
    }

}
