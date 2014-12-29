package com.qingfengmy.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingfengmy.R;

/**
 * User: zhangtao
 * Date: 2014-11-17
 * Time: 16:40
 */
public class SingleView extends LinearLayout implements Checkable {

    private TextView mText;
    private CheckBox mCheckBox;

    public SingleView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        // 填充布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_single_layout, this, true);
        mText = (TextView) v.findViewById(R.id.title);
        mCheckBox = (CheckBox) v.findViewById(R.id.checkbox);
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    public void toggle() {
        mCheckBox.toggle();
    }

    public void setTitle(String text) {
        mText.setText(text);
    }

}