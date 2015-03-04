package com.qingfengmy.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.fragment.TabHostFragment;
import com.qingfengmy.ui.view.BadgeView;
import com.r0adkll.slidr.Slidr;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TabHostActivity extends BaseActivity {

    private FragmentTabHost tabHost;
    private LayoutInflater inflater;

    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;

    private ImageView shopcart;
    private BadgeView badgeView;
    private int num;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        ButterKnife.inject(this);
        Slidr.attach(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        // 设置fragment显示在那个layout
        tabHost.setup(this, getSupportFragmentManager(), R.id.host_content);

        Bundle bundle1 = new Bundle();
        StartShake shake = new StartShake();
        bundle1.putSerializable("shake", shake);
        bundle1.putString("name", "推荐");

        Bundle bundle2 = new Bundle();
        bundle2.putSerializable("shake", shake);
        bundle2.putString("name", "首页");

        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("shake", shake);
        bundle3.putString("name", "购物车");

        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("shake", shake);
        bundle4.putString("name", "我的");

        Bundle bundle5 = new Bundle();
        bundle5.putSerializable("shake", shake);
        bundle5.putString("name", "更多");

        // tag让开发者看的
        TabSpec spec1 = tabHost.newTabSpec("tag1");
        // 设置底栏的标签图片
        view1 = getTabItemView(R.drawable.ative1, "推荐");
        spec1.setIndicator(view1);
        // 设置内容
        tabHost.addTab(spec1, TabHostFragment.class, bundle1);

        TabSpec spec2 = tabHost.newTabSpec("tag2");
        view2 = getTabItemView(R.drawable.ative2, "首页");
        spec2.setIndicator(view2);
        tabHost.addTab(spec2, TabHostFragment.class, bundle2);

        TabSpec spec3 = tabHost.newTabSpec("tag3");
        view3 = getTabItemView(R.drawable.ative3, "购物车");
        spec3.setIndicator(view3);
        tabHost.addTab(spec3, TabHostFragment.class, bundle3);

        TabSpec spec4 = tabHost.newTabSpec("tag4");
        view4 = getTabItemView(R.drawable.ative4, "我的");
        spec4.setIndicator(view4);
        tabHost.addTab(spec4, TabHostFragment.class, bundle4);

        TabSpec spec5 = tabHost.newTabSpec("tag5");
        view5 = getTabItemView(R.drawable.ative5, "更多");
        spec5.setIndicator(view5);
        tabHost.addTab(spec5, TabHostFragment.class, bundle5);

    }

    private View getTabItemView(int img, String text) {
        View view = inflater.inflate(R.layout.item_tabhost, null);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.item_tabhost_img);
        TextView textView = (TextView) view
                .findViewById(R.id.item_tabhost_text);

        if (text.equals("购物车")) {
            shopcart = imageView;
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.item_tabhostlayout);
            badgeView = new BadgeView(this, layout);
            badgeView.setTextColor(Color.WHITE);
            badgeView.setBackgroundColor(Color.parseColor("#59b984"));
            badgeView.setTextSize(12);
        }

        imageView.setImageResource(img);
        textView.setText(text);
        return view;
    }

    public class StartShake implements Serializable {
        public void startShake(View view) {
            // 起始位置
            int[] startLocation = new int[2];
            view.getLocationInWindow(startLocation);
            // 终止位置
            int[] endLocation = new int[2];
            shopcart.getLocationInWindow(endLocation);
            // 偏移位移
            int xx = endLocation[0] - startLocation[0];
            int yy = endLocation[1] - startLocation[1];
            // 构建动画层(在屏幕上建立动画层，使view在该层上运动)
            ViewGroup rootView = createAnimLayout();
            // 动画层添加商品图标
            View shopView = addShopView(rootView, startLocation);

            ObjectAnimator animator_x = ObjectAnimator.ofFloat(shopView, "translationX", 0, xx).setDuration(1000);
            animator_x.setInterpolator(new LinearInterpolator());
            ObjectAnimator animator_y = ObjectAnimator.ofFloat(shopView, "translationY", 0, yy).setDuration(1000);
            animator_y.setInterpolator(new AnticipateInterpolator());

            AnimatorSet set = new AnimatorSet();
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Animator anim = AnimatorInflater.loadAnimator(TabHostActivity.this, R.anim.shake);
                    anim.setTarget(shopcart);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            num++;
                            badgeView.setText(num + "");
                            badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                            badgeView.setBadgeBackgroundColor(0x59b984);
                            badgeView.show();
                        }
                    });
                    anim.start();

                    // 完成后移出view
                    decorView.removeView(animLayout);
                }
            });
            set.playTogether(animator_x, animator_y);
            set.start();
        }

    }

    ViewGroup decorView;
    LinearLayout animLayout;

    // 创建动画层
    private ViewGroup createAnimLayout() {
        // 获取根view
        decorView = (ViewGroup) this.getWindow().getDecorView();
        // 子view设置参数
        animLayout = new LinearLayout(this);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        // 添加子view
        decorView.addView(animLayout);
        return animLayout;
    }

    // 动画层添加商品view
    private View addShopView(ViewGroup rootView, int[] startLocation) {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.ic_launcher);
        rootView.addView(imageView);

        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lp.leftMargin = startLocation[0];
        lp.topMargin = startLocation[1];
        imageView.setLayoutParams(lp);
        return imageView;
    }
}
