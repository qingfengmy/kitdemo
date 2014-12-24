package com.qingfengmy.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.view.FlowLayoutView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 10:04
 */
public class FlowLayoutActivity extends BaseActivity {
    private String[] mVals = new String[]
            {"Hello", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};

    @InjectView(R.id.flowlayout)
    FlowLayoutView mFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        ButterKnife.inject(this);

        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.text_flowlayout,
                    mFlowLayout, false);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);
        }
    }
}
