package com.qingfengmy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/16.
 */
public class EmptyView extends RelativeLayout {


    Context context;
    private LayoutInflater inflater;
    @InjectView(R.id.empty_text)
    TextView textView;

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        View localView = this.inflater.inflate(R.layout.layout_empty, this);
        ButterKnife.inject(this, localView);
    }

    public EmptyView(Context context) {
        this(context, null);
    }

    public void setText(String text){
        if(textView != null)
            textView.setText(text);
    }
}
