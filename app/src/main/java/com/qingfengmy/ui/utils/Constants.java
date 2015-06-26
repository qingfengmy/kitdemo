package com.qingfengmy.ui.utils;

/**
 * Created by Administrator on 2015/1/21.
 */
public class Constants {

    public static boolean DEBUG = true;

    public static enum Action {
        ACTION_START,
        ACTION_PAUSE,
        ACTION_UPDATE,
        ACTION_FINISHED,
        ACTION_FAILED
    }

    public static final int GET_PIC_FROM_PHOTO_REQUEST = 1;
    public static final int GET_PIC_FROM_CAMERA_REQUEST = 2;
    public static final int CROP_PIC_REQUEST = 3;
}
