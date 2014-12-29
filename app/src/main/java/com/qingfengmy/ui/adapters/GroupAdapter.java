package com.qingfengmy.ui.adapters;

import java.util.ArrayList;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qingfengmy.R;

public class GroupAdapter extends BaseAdapter {

	private Activity activity;// 上下文
	private ArrayList<String> list;

	private LayoutInflater inflater = null;// 导入布局
	private int temp = 0;

	public GroupAdapter(Activity context, ArrayList<String> list) {
		this.activity = context;
		this.list = list;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// listview每显示一行数据,该函数就执行一次
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {// 当第一次加载ListView控件时 convertView为空
			convertView = inflater.inflate(R.layout.item_sigleselecter, null);// 所以当ListView控件没有滑动时都会执行这条语句
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.rb = (RadioButton) convertView.findViewById(R.id.item_cb);
			convertView.setTag(holder);// 为view设置标签

		} else {// 取出holder
			holder = (ViewHolder) convertView.getTag();// the Object stored in
														// this view as a tag
		}
		// 设置list的textview显示
		holder.tv.setText(list.get(position));
		// 根据isSelected来设置checkbox的选中状况

		holder.rb.setId(position);// 对checkbox的id进行重新设置为当前的position
		holder.rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			// 把上次被选中的checkbox设为false
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// 实现checkbox的单选功能,同样适用于radiobutton
					if (temp != -1) {
						// 找到上次点击的checkbox,并把它设置为false,对重新选择时可以将以前的关掉
						RadioButton tempRadio = (RadioButton) activity
								.findViewById(temp);
						if (tempRadio != null)
							tempRadio.setChecked(false);
					}
					temp = buttonView.getId();// 保存当前选中的checkbox的id值
				}
			}
		});
		if (position == temp)// 比对position和当前的temp是否一致
			holder.rb.setChecked(true);
		else
			holder.rb.setChecked(false);
		return convertView;
	}

	public class ViewHolder {
		public TextView tv;
		public RadioButton rb;
	}

	public int getTemp(){
		return temp;
	}
}
