package com.qingfengmy.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/9.
 */
public class TransitionActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    String[] list = new String[]{"explode：从场景的中心移入或移出 ", "slide：从场景的边缘移入或移出 ", "fade：调整透明度产生渐变效果", "share：分享元素", "完整demo"};

    Window window;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. 允许使用transition(requestFeature() must be called before adding content)
        window = getWindow();
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition);
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

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter());

    }

    @Override
    public void finishAfterTransition() {
        // 4. 返回之后，这里执行reenter transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Material主题默认会将exit的共享元素transition设置成null而enter的共享元素transition设置成@android:transition/move.如果reenter 或者 return transition没有明确设置，则将用exit 和enter的共享元素transition替代
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.row_application, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.name.setText(list[position]);
            if (position != 4) {
                holder.image.setVisibility(View.GONE);
            }

            TransitionActivity.this.position = position;

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // 2. 设置具体transition
                        switch (position) {
                            case 0:
                                window.setExitTransition(new Explode());
                                break;
                            case 1:
                                window.setExitTransition(new Slide());
                                break;
                            case 2:
                                window.setExitTransition(new Fade());
                                break;
                            case 3:
                                window.setExitTransition(new Slide().setDuration(2000));
                                break;
                            case 4:
                                Transition ts = new ChangeTransform();
                                ts.setDuration(1000);
                                window.setExitTransition(ts);
                                break;
                        }
                        // 3. 开始一个activity
                        Bundle bundle;
                        Intent intent = new Intent();
                        if (position == 3) {
                            Pair<View, String> pair1 = Pair.create((View) holder.name, "text");
                            bundle = ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this, pair1).toBundle();
                            intent.setClass(TransitionActivity.this, TransitionExplodeActivity.class);
                            intent.putExtra("position", position);
                        } else if (position == 4) {
                            Pair<View, String> pair1 = Pair.create((View) holder.image, "img");
                            bundle = ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this, pair1).toBundle();
                            intent.setClass(TransitionActivity.this, TransitionDemoActivity.class);
                        } else {
                            bundle = ActivityOptions.makeSceneTransitionAnimation(TransitionActivity.this).toBundle();
                            intent.setClass(TransitionActivity.this, TransitionExplodeActivity.class);
                            intent.putExtra("position", position);
                        }
                        startActivity(intent, bundle);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @InjectView(R.id.cardview)
            CardView card;

            @InjectView(R.id.countryImage)
            ImageView image;

            @InjectView(R.id.countryName)
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.inject(this, itemView);
            }
        }
    }

}
