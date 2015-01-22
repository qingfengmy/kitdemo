package com.qingfengmy.ui.app;


import com.activeandroid.ActiveAndroid;
import com.activeandroid.app.Application;

/**
 * Created by Administrator on 2015/1/22.
 */
public class KitDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
