package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 17:50
 */
public class MyAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    List<String> titles;

    public MyAdapter(Context context, List<String> titles) {
        this.titles = titles;
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_tabhost_fragment,
                    null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }

        holder.name.setText(titles.get(position));
        return convertView;
    }

    static class ViewHolder {

        @InjectView(R.id.item_tabhost_fragment_img)
        ImageView img;

        @InjectView(R.id.item_tabhost_fragment_name)
        TextView name;
        @InjectView(R.id.item_tabhost_fragment_add)
        ImageView add;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}