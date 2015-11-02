package com.qingfengmy.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qingfengmy.R;
import com.qingfengmy.ui.AppBarLayoutActivity;
import com.qingfengmy.ui.CollapsingToolbarLayoutActivity;
import com.qingfengmy.ui.TabLayoutActivity;
import com.qingfengmy.ui.TextInputLayoutActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/3.
 */
public class MNCFragment extends Fragment {

    @InjectView(R.id.listView)
    ListView listView;

    private String[] titles = {
            "TextInputLayout",
            "FloatingActionButton",
            "SnackBar",
            "tablayout",
            "AppBarLayout",
            "CollapsingToolbarLayout"};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lollipop, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        // textinputlayout, floatingActionBar,SnackBar
                        startActivity(new Intent(getActivity(), TextInputLayoutActivity.class));
                        break;
                    case 3:
                        // tablayout
                        startActivity(new Intent(getActivity(), TabLayoutActivity.class));
                        break;
                    case 4:
                        // appbarlayout
                        startActivity(new Intent(getActivity(), AppBarLayoutActivity.class));
                        break;
                    case 5:
                        // CollapsingToolbarLayout
                        startActivity(new Intent(getActivity(), CollapsingToolbarLayoutActivity.class));
                        break;
                }
            }
        });
    }
}
