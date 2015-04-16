package com.qingfengmy.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.qingfengmy.R;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import java.util.Calendar;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/4/15.
 */
public class DatePickerFragment extends Fragment implements DatePicker.OnDateChangedListener {

    @InjectView(R.id.datePicker)
    DatePicker datePicker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_datepicker, null);
        ButterKnife.inject(this, view);

        //创建一个日历引用calendar，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        Calendar calendar = Calendar.getInstance(Locale.CHINA);

        int year         = calendar.get(Calendar.YEAR);
        int monthOfYear  = calendar.get(Calendar.MONTH);
        int dayOfMonth   = calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, this);

        return view;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        ToastUtil.showToast(getActivity(), "您选择了" + year + "年" + monthOfYear + "月" + dayOfMonth + "日");
    }
}
