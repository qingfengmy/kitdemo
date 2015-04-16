package com.qingfengmy.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.qingfengmy.R;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/4/15.
 */
public class TimePickerFragment extends Fragment implements  TimePicker.OnTimeChangedListener{
    @InjectView(R.id.timePicker)
    TimePicker timePicker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_picker, null);
        ButterKnife.inject(this, view);
        timePicker.setOnTimeChangedListener(this);
        return view;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        ToastUtil.showToast(getActivity(), "您选择了" + hourOfDay + "小时" + minute + "分");
    }
}
