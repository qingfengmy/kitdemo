package com.qingfengmy.ui;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.DragFrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/2/10.
 */
public class ElevationDragActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.circle)
    View circle;
    @InjectView(R.id.raise_bt)
    Button raise;
    @InjectView(R.id.lower_bt)
    Button lower;
    /* The circular outline provider */
    private ViewOutlineProvider mOutlineProviderCircle;
    @InjectView(R.id.main_layout)
    DragFrameLayout dragLayout;

    /* The current elevation of the floating view. */
    private float mElevation = 0;

    /* The step in elevation when changing the Z value */
    private int mElevationStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elevation);

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
            circle.setClipToOutline(true);
            mOutlineProviderCircle = new OutlineProvider();
            circle.setOutlineProvider(mOutlineProviderCircle);
        }

        dragLayout.setDragFrameController(new DragFrameLayout.DragFrameLayoutController() {

            @Override
            public void onDragDrop(boolean captured) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //  通过动画的方式让视图的阴影增大和减小
                    circle.animate().translationZ(captured ? 50 : 0).setDuration(100);
                }
            }
        });

        dragLayout.addDragView(circle);
        mElevationStep = getResources().getDimensionPixelSize(R.dimen.margin15);
    }

    @OnClick(R.id.raise_bt)
    public void raise() {
        mElevation += mElevationStep;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            circle.setElevation(mElevation);
    }

    @OnClick(R.id.lower_bt)
    public void lower() {
        mElevation -= mElevationStep;
        if (mElevation < 0) {
            mElevation = 0;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circle.setElevation(mElevation);
        }

    }


    private int style;

    /**
     * ViewOutlineProvider which sets the outline to be an oval which fits the view bounds.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class OutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            switch (style) {
                case 0:
                    // 圆
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                    break;
                case 1:
                    // 矩形
                    outline.setRect(0, 0, view.getWidth(), view.getHeight());
                    break;
                case 2:
                    // 圆角矩形
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 10);
                    break;
                case 3:
                    // 椭圆
                    outline.setOval(0, 0, view.getWidth() * 2 / 3, view.getHeight());
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_elevation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                style = (style + 1) % 4;
                circle.invalidateOutline();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
