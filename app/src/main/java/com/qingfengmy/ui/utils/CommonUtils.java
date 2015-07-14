package com.qingfengmy.ui.utils;

import android.content.Context;

import com.qingfengmy.R;

/**
 * Created by Administrator on 2015/6/29.
 */
public class CommonUtils {

    public static String getImageString(Context mContext, String img) {
        String a = "";
        String b = "";
        String c = img;
        if (c.startsWith("app")) {
            b = c.substring(3, c.indexOf("."));
        } else {
            b = c.substring(0, c.indexOf("."));
        }
        a = b.substring(0, 5);
        return String.format(mContext.getString(R.string.image_url), a, b, c);
    }

    public static String capitalize(final String word) {
        if (word.length() > 1) {
            return String.valueOf(word.charAt(0)).toUpperCase() + word.substring(1);
        }
        return word;
    }
}
