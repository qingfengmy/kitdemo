package com.qingfengmy.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
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
import com.qingfengmy.ui.MainActivity;
import com.qingfengmy.ui.SlidingMenuActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-30
 * Time: 11:38
 */
public class MenuFragment extends Fragment {
    private NavigationDrawerCallbacks mCallback;

    private boolean translation;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (NavigationDrawerCallbacks) activity;

            if(activity instanceof SlidingMenuActivity){
                translation = true;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_menu, null);
        if(!translation)
            view.setBackgroundColor(Color.WHITE);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick({R.id.id_layout1, R.id.id_layout2, R.id.id_layout3, R.id.id_layout4, R.id.id_layout5, R.id.id_layout6})
    public void click(View v) {
        int position = 0;
        switch (v.getId()) {
            case R.id.id_layout1:
                position = R.id.nav_home;
                break;
            case R.id.id_layout2:
                position = R.id.nav_about;
                break;
            case R.id.id_layout3:
                position = R.id.nav_joke;
                break;
            case R.id.id_layout4:
                position = R.id.nav_joke_img;
                break;
            case R.id.id_layout5:
                position = R.id.nav_android_l;
                break;
            case R.id.id_layout6:
                position = R.id.nav_game;
                break;
        }
        if (mCallback != null) {
            mCallback.onNavigationDrawerItemSelected(position);
        }
    }


    public interface NavigationDrawerCallbacks {
        public void onNavigationDrawerItemSelected(int position);
    }

}
