package com.qingfengmy.ui;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ClippingActivity extends BaseActivity {
    /* The {@Link Outline} used to clip the image with. */
    private ViewOutlineProvider mOutlineProvider;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.text)
    TextView clippedView;
    @InjectView(R.id.change)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /* Sets the OutlineProvider for the View. */
            mOutlineProvider = new ClipOutlineProvider();
            clippedView.setOutlineProvider(mOutlineProvider);
        }


    }

//    boolean state;
//    @OnClick(R.id.text)
//    public void clickText() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if (state) {
//                clippedView.setText("梳洗罢，独倚望江楼，过尽千帆皆不是，斜晖脉脉水悠悠，肠断白蘋洲。");
//            } else {
//                clippedView.setText("因何而生，因何而战！");
//            }
//            clippedView.invalidateOutline();
//            state = !state;
//        }
//    }

    @OnClick(R.id.change)
    public void clip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Toggle whether the View is clipped to the outline
            if (clippedView.getClipToOutline()) {
                    /* The Outline is set for the View, but disable clipping. */
                clippedView.setClipToOutline(false);

                button.setText("剪切");
            } else {
                    /* Enables clipping on the View. */
                clippedView.setClipToOutline(true);

                button.setText("禁止剪切");
            }
        }
    }

    /**
     * A {@link ViewOutlineProvider} which clips the view with a rounded rectangle which is inset
     * by 10%
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class ClipOutlineProvider extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            final int margin = Math.min(view.getWidth(), view.getHeight()) / 10;
            outline.setRoundRect(margin, margin, view.getWidth() - margin,
                    view.getHeight() - margin, margin / 2);
        }

    }
}