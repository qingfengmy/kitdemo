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
import com.qingfengmy.ui.CardViewActivity;
import com.qingfengmy.ui.ClippingActivity;
import com.qingfengmy.ui.ElevationDragActivity;
import com.qingfengmy.ui.PaletteActivity;
import com.qingfengmy.ui.RecyclerActivity;
import com.qingfengmy.ui.StateListAnimatorActivity;
import com.qingfengmy.ui.SvgActivity;
import com.qingfengmy.ui.TintingsActivity;
import com.qingfengmy.ui.TransitionActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/2/3.
 */
public class LollipopFragment extends Fragment {

    @InjectView(R.id.listView)
    ListView listView;

    private String[] titles = {
            "recycle view",
            "card view",
            "取色：palette",
            "Activity Transition",
            "定义阴影:Z = elevation + translationZ",
            "裁剪：ViewOutlineProvider",
            "Drawable Tinting（着色）",
            "矢量图动画",
            "View state changes （视图状态改变）"};

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
                        // recycler view
                        startActivity(new Intent(getActivity(), RecyclerActivity.class));
                        break;
                    case 1:
                        // card view
                        startActivity(new Intent(getActivity(), CardViewActivity.class));
                        break;
                    case 2:
                        // palette
                        startActivity(new Intent(getActivity(), PaletteActivity.class));
                        break;
                    case 3:
                        // transition
                        startActivity(new Intent(getActivity(), TransitionActivity.class));
                        break;
                    case 4:
                        // 阴影
                        startActivity(new Intent(getActivity(), ElevationDragActivity.class));
                        break;
                    case 5:
                        // 裁剪
                        startActivity(new Intent(getActivity(), ClippingActivity.class));
                        break;
                    case 6:
                        // 着色
                        startActivity(new Intent(getActivity(), TintingsActivity.class));
                        break;
                    case 7:
                        // svg
                        startActivity(new Intent(getActivity(), SvgActivity.class));
                        break;
                    case 8:
                        // View state changes （视图状态改变）
                        startActivity(new Intent(getActivity(), StateListAnimatorActivity.class));
                        break;
                }
            }
        });
    }
}
