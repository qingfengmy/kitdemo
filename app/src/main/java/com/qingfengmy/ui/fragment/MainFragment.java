package com.qingfengmy.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * User: zhangtao
 * Date: 2014-12-15
 * Time: 10:53
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener{

    @InjectView(R.id.listview)
    ListView listview;
    private ArrayAdapter<String> adapter;
    BaseActivity mActivity;

    public String[] names;
    public String[] classes;

    /**
     * fragment的生命周期中，构造方法-onAttach-onCreateView-onActivityCreate
     * 其中在onAttach之后，才会获取activity
     * 所以建议渲染操作放在onActivityCreated中
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();

        names = getResources().getStringArray(R.array.names);
        classes = getResources().getStringArray(R.array.classes);

        adapter = new ArrayAdapter<String>(mActivity,
                android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> clazz;
        Intent intent = new Intent();
        try {
            clazz = Class.forName(classes[position]);
            intent.setClass(mActivity, clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            mActivity.showToast("不存在该类");
        }
    }
}
