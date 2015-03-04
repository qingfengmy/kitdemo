package com.qingfengmy.ui.game.bean;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2015/3/3.
 */
public class ImagePiece {

    private int index;
    private Bitmap bitmap;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public String toString() {
        return "ImagePiece{" +
                "index=" + index +
                ", bitmap=" + bitmap +
                '}';
    }
}
