package com.qingfengmy.ui.entity;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aspsine on 15-4-19.
 */
@Table(name = "fileInfo")
public class FileInfo  extends Model implements Serializable {

    public static final int pause = 0;
    public static final int start = 1;
    public static final int finished = 2;
    @Column(name = "fid")
    private int fId;

    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "length")
    private int length;
    @Column(name = "finshed")
    private int finshed;

    @Column(name = "statue")
    private int statue;

    public FileInfo() {
        super();
    }

    public FileInfo(int fid, String url, String name, int length, int finshed) {
        super();
        this.fId = fid;
        this.url = url;
        this.name = name;
        this.length = length;
        this.finshed = finshed;
    }

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public int getfId() {
        return fId;
    }

    public void setfId(int fId) {
        this.fId = fId;
    }

    public int getFinshed() {
        return finshed;
    }

    public void setFinshed(int finshed) {
        this.finshed = finshed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", length=" + length +
                '}';
    }


    public List<FileInfo> getFileInfos() {
        return new Select().from(FileInfo.class).execute();
    }

    public FileInfo existFileInfo(String url){
        return new Select().from(FileInfo.class).where("url=?", url).executeSingle();
    }

    public int getHasfinished(String url){
        FileInfo info = new Select().from(FileInfo.class).where("url=?", url).executeSingle();
        if (info != null){
            return (int) (info.getFinshed()*100.00/info.getLength());
        }else {
            return 0;
        }
    }

    public int getDownloadStatue(String url){
        FileInfo info = new Select().from(FileInfo.class).where("url=?", url).executeSingle();
        if (info != null){
            return info.getStatue();
        }else {
            return 0;
        }
    }


    public synchronized void update(String url, int finished, int statue) {
        FileInfo fileInfo = existFileInfo(url);
        fileInfo.setFinshed(finished);
        fileInfo.setStatue(statue);
        fileInfo.save();
    }
}
