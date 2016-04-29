package com.qingfengmy.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/9.
 */
public class TransitionExplodeActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @InjectView(R.id.textView)
    TextView text;

    Window window;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. 设置允许transition
        window = getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_explode);

        ButterKnife.inject(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        position = getIntent().getIntExtra("position", 0);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            switch (position){
                case 0:// 2. 设置具体的transition
                    window.setEnterTransition(new Explode());
                    // 3. 设置返回的transition
                    window.setReturnTransition(new Explode());
                    break;
                case 1:// 2. 设置具体的transition
                    window.setEnterTransition(new Slide());
                    // 3. 设置返回的transition
                    window.setReturnTransition(new Slide());
                    break;
                case 2:// 2. 设置具体的transition
                    window.setEnterTransition(new Fade());
                    // 3. 设置返回的transition
                    window.setReturnTransition(new Fade());
                    break;
                case 3:
                    // 共享元素
                    // 在两个activity的样式文件中给共享元素分配一个相同的名字使用android:transitionName属性
                    text.setTransitionName("text");
                    window.setSharedElementEnterTransition(new Slide().setDuration(1000));
                    break;
            }

        }
    }


}
