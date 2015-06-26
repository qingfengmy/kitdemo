

package com.qingfengmy.ui.utils.tools;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    public static final String ALL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static long formatTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ALL_FORMAT);
        long modified = 0;
        try {
            Date date = sdf.parse(time);
            modified = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return modified;
    }

    public static String formatTime(long time) {
        time = time * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(ALL_FORMAT);
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String formatTime(Date date) {
        if (TextUtils.isEmpty(ALL_FORMAT) || date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(ALL_FORMAT);
        return sdf.format(date);
    }

}
