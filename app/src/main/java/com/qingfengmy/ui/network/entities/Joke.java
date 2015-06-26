package com.qingfengmy.ui.network.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/1/21.
 */
@Table(name = "joke")
public class Joke extends Model implements Serializable {
    @Column(name = "content")
    @SerializedName("content")
    private String content;

    @Column(name = "image")
    @SerializedName("image")
    private String image;

    @Column(name = "jokeId")
    @SerializedName("id")
    private String jokeId;

    @Column(name = "time")
    @SerializedName("created_at")
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJokeId() {
        return jokeId;
    }

    public void setJokeId(String jokeId) {
        this.jokeId = jokeId;
    }
}
