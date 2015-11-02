package com.qingfengmy.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 17:50
 */
public class SampleAdapter extends BaseRecyclerViewAdapter {

    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public SampleAdapter() {
        list = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).name.setText(String.valueOf(list.get(position)));
        }
    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabhost_fragment, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ItemViewHolder(view);
    }

    static class ItemViewHolder extends ViewHolder{

        @InjectView(R.id.item_tabhost_fragment_img)
        ImageView img;

        @InjectView(R.id.item_tabhost_fragment_name)
        TextView name;
        @InjectView(R.id.item_tabhost_fragment_add)
        ImageView add;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
