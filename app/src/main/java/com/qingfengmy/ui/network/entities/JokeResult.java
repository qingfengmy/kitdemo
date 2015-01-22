package com.qingfengmy.ui.network.entities;

import java.util.List;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeResult {
    private List<Joke> data;

    public List<Joke> getJokeList() {
        return data;
    }

    public void setJokeList(List<Joke> jokeList) {
        this.data = jokeList;
    }
}
