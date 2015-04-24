package com.qingfengmy.ui.entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aspsine on 15-4-19.
 */
@Table(name = "threadInfo")
public class ThreadInfo extends Model implements Serializable {
    @Column(name = "url")
    private String url;
    @Column(name = "start")
    private int start;
    @Column(name = "end")
    private int end;
    @Column(name = "finshed")
    private int finshed;


    public ThreadInfo() {
    }

    public ThreadInfo(String url, int start, int end, int finshed) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.finshed = finshed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinshed() {
        return finshed;
    }

    public void setFinshed(int finshed) {
        this.finshed = finshed;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finshed=" + finshed +
                '}';
    }

    public List<ThreadInfo> getThreadInfos(String url) {
        return new Select().from(ThreadInfo.class).where("url=?", url).execute();
    }

    public boolean exists(String url) {
        return new Select().from(ThreadInfo.class).where("url=?", url).executeSingle() == null ? false : true;
    }

    public void delete(String url) {
        new Delete().from(ThreadInfo.class).where("url=?", url).execute();
    }

    public void update(String url, int finished) {
        this.url = url;
        this.finshed = finished;
        this.save();
    }
}