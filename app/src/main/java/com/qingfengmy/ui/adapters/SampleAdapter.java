package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.v7.widget.RecyclerView.*;

/**
 * User: zhangtao
 * Date: 2014-12-24
 * Time: 17:50
 */
public class SampleAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Integer> list;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public List<Integer> getList() {
        return list;
    }

    public SampleAdapter() {
        list = new ArrayList<Integer>();
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).name.setText(String.valueOf(list.get(position)));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tabhost_fragment, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footerview, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }

        return null;
    }

    class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

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
