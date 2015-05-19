package com.qingfengmy.ui.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2015/1/21.
 */
public class Constants {
    public static final String OpenID = "ed997333bee7eadc800a21adae33ba72";

    public static boolean DEBUG = true;

    public static enum Action {
        ACTION_START,
        ACTION_PAUSE,
        ACTION_UPDATE,
        ACTION_FINISHED,
        ACTION_FAILED
    }

    public static final String APIKEY = "3a4b33974f9b28fbf779176c41d7dda9";
    public static final String APISECRET = "cGypoX7yxhs9G4YcwlL7Vk0Rs-nNxj96";
    public static final int GET_PIC_FROM_PHOTO_REQUEST = 1;
    public static final int GET_PIC_FROM_CAMERA_REQUEST = 2;
    public static final int CROP_PIC_REQUEST = 3;
}
