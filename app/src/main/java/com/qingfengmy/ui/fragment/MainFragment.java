package com.qingfengmy.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengmy.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * User: zhangtao
 * Date: 2014-12-15
 * Time: 10:53
 */
public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.inject(this,view);
        return view;
    }

    @OnClick(R.id.text_main)
    public void click() {
        Toast.makeText(getActivity(), "fragment被电击了", Toast.LENGTH_SHORT).show();
        Log.e("aa","aa");
    }
}