package com.qingfengmy.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-25
 * Time: 16:12
 */
public class AnimatorActivity extends BaseActivity {

    int[] ids = new int[]

            {
                    R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5, R.id.icon6, R.id.icon7
            };

    ImageView[] imgs = new ImageView[7];

    boolean flag = true;

    int[] ids_left = new int[]

            {
                    R.id.icon1_left, R.id.icon2_left, R.id.icon3_left, R.id.icon4_left, R.id.icon5_left
            };

    ImageView[] imgs_left = new ImageView[5];

    boolean flag_left = true;

    @InjectView(R.id.startfloat)
    Button startfloat;

    @InjectView(R.id.charline)
    Button startchar;
    String[] lines = {"*", "地", "震", "高", "岗", "一", "派", "青", "山", "千", "古", "秀", "门", "朝", "大", "海", "三", "河", "河", "水", "万", "年", "流", "!"};
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        ButterKnife.inject(this);
        setSupportActionBar(titleBar);
        titleBar.setTitle(names[3]);
        titleBar.setNavigationIcon(R.drawable.ic_launcher);
        for (int i = 0; i < 7; i++) {
            imgs[i] = (ImageView) findViewById(ids[i]);
        }
        for (int i = 0; i < 5; i++) {
            imgs_left[i] = (ImageView) findViewById(ids_left[i]);
        }
    }

    String line = "";
    String last;

    @OnClick(R.id.charline)
    public void line() {
        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<String>() {
            @Override
            public String evaluate(float fraction, String startValue, String endValue) {
                // fraction是从0-1变化
                int i = (int) ((lines.length-1) * fraction);
                return lines[i];
            }
        }, "*", "!");

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String value = (String) animation.getAnimatedValue();
                if (!value.equals(last)) {
                    line = line + value;
                    startchar.setText(line);
                }

                last = value;
            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    @OnClick(R.id.startfloat)
    public void start() {
        // value动画
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        // 监听每一步变化的值
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                startfloat.setText(animation.getAnimatedValue().toString());
            }
        });
        // 从0到12306.99共需5秒
        animator.setDuration(5000);
        // 加速变化
        animator.setInterpolator(new AnticipateInterpolator());
        animator.start();
    }

    @OnClick(R.id.button)
    public void start(View view) {
        if (flag) {
            startAnimator();
        } else {
            finishAnimator();
        }
    }

    private void finishAnimator() {
        for (int i = 0; i < 7; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imgs[i], "translationY", 120f * (i + 1), 0f);
            animator.setDuration(500);
            // 设置加速动画插补器
            animator.setInterpolator(new AnticipateInterpolator());
            // 设置动画延迟
            animator.setStartDelay(300 * i);
            animator.start();
        }
        flag = true;
    }

    private void startAnimator() {

        for (int i = 0; i < 7; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(imgs[i], "translationY", 0f, 120f * (i + 1));
            animator.setDuration(500);
            // 设置弹跳动画插补器
            animator.setInterpolator(new BounceInterpolator());
            // 设置动画延迟
            animator.setStartDelay(300 * i);
            animator.start();
        }
        flag = false;
    }

    @OnClick(R.id.button_left)
    public void startLeft(View view) {
        if (flag) {
            startAnimatorLeft();
        } else {
            finishAnimatorLeft();
        }
    }

    int r;

    private void finishAnimatorLeft() {
        r = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (int i = 0; i < 5; i++) {
            double offangle = 90 / (5 - 1);
            float deltaY, deltaX;
            deltaY = (float) (Math.sin(offangle * i * Math.PI / 180) * r);
            deltaX = (float) (Math.cos(offangle * i * Math.PI / 180) * r);

            ObjectAnimator animatorx = ObjectAnimator.ofFloat(imgs_left[i], "translationX", deltaX, 0f);
            ObjectAnimator animatory = ObjectAnimator.ofFloat(imgs_left[i], "translationY", -deltaY, 0f);
            animatorx.setDuration(500);
            animatory.setDuration(500);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(animatorx, animatory);
            set.setInterpolator(new AnticipateInterpolator());
            set.start();
        }
        flag = true;
    }

    private void startAnimatorLeft() {
        r = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        for (int i = 0; i < 5; i++) {
            double offangle = 90 / (5 - 1);
            float deltaY, deltaX;
            deltaY = (float) (Math.sin(offangle * i * Math.PI / 180) * r);
            deltaX = (float) (Math.cos(offangle * i * Math.PI / 180) * r);

            ObjectAnimator animatorx = ObjectAnimator.ofFloat(imgs_left[i], "translationX", 0f, deltaX);
            ObjectAnimator animatory = ObjectAnimator.ofFloat(imgs_left[i], "translationY", 0f, -deltaY);
            animatorx.setDuration(500);
            animatory.setDuration(500);

            AnimatorSet set = new AnimatorSet();
            // 设置弹跳动画插补器
            set.playTogether(animatorx, animatory);
            set.setInterpolator(new OvershootInterpolator());
            set.start();
        }
        flag = false;
    }
}
