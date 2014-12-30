package com.qingfengmy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-30
 * Time: 11:38
 */
public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_menu, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @OnClick({R.id.id_layout1, R.id.id_layout2, R.id.id_layout3, R.id.id_layout4, R.id.id_layout5, R.id.id_layout6})
    public void click(View v){
        RelativeLayout layout = (RelativeLayout) v;
        TextView t = (TextView) layout.getChildAt(1);
        Toast.makeText(getActivity(), t.getText()+"被电击了", Toast.LENGTH_SHORT).show();
    }
}
