package com.qingfengmy.ui.network.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2015/1/21.
 */
@Table(name = "joke")
public class Joke extends Model {
    @Column(name = "content")
    private String content;
    @Column(name = "hashId")
    private String hashId;
    @Column(name = "unixtime")
    private long unixtime;
    @Column(name = "updatetime")
    private String updatetime;
    @Column(name = "url")
    private String url;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public long getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(long unixtime) {
        this.unixtime = unixtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}