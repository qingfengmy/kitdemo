package com.qingfengmy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/5.
 */
public class SplashActivity extends BaseActivity {

    @InjectView(R.id.splash)
    ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
//        getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);  //设置全屏
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        splash.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//
//        ObjectAnimator o1 = ObjectAnimator.ofFloat(img, "alpha", 1f, 0.5f).setDuration(2000);
//        ObjectAnimator o2 = ObjectAnimator.ofFloat(img, "scaleX", 1f, 2f).setDuration(2000);
//        ObjectAnimator o3 = ObjectAnimator.ofFloat(img, "scaleY", 1f, 2f).setDuration(2000);
//
//        AnimatorSet set = new AnimatorSet();
//        set.playTogether(o1, o2, o3);
//        set.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                finish();
//            }
//        });
//        set.start();
        splash.postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
