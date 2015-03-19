package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.qingfengmy.R;

/**
 * Created by Administrator on 2015/3/5.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();;
                    }
                }
                , 2000);
    }
}
