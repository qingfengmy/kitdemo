package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengmy.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MultipeSelectAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> productsList;
    private ListView mListView;
    LayoutInflater mInflater;
    private SelectAllListenner selectAllListenner;

    public MultipeSelectAdapter(Context mContext,
                                List<String> productsList, ListView listview) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.mListView = listview;
        mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
    }

    public void setSelectAllListenner(SelectAllListenner selectAllListenner) {
        this.selectAllListenner = selectAllListenner;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = mInflater.inflate(R.layout.item_multipe_select,
                    null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }

        final String product = productsList.get(position);

        holder.goods_summery.setText(product);

        holder.product_select_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListView.setItemChecked(position, !mListView.isItemChecked(position));
                selectAllListenner.clickSelectAll(checkSelectAll());
            }
        });

        updateBackground(position, holder.product_select_img);

        return convertView;
    }

    public void updateBackground(int position, View view) {
        if (mListView.isItemChecked(position)) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
    }

    public boolean checkSelectAll() {
        boolean result = true;
        for (int i = 0; i < mListView.getCount(); i++) {
            if (!mListView.isItemChecked(i)) {
                // 有一个是false
                result = false;
            }
        }
        return result;
    }

    static class ViewHolder {


        @InjectView(R.id.goods_summery)
        TextView goods_summery;

        @InjectView(R.id.product_select_layout)
        RelativeLayout product_select_layout;

        @InjectView(R.id.product_select_img)
        ImageView product_select_img;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public interface SelectAllListenner {
        public void clickSelectAll(boolean selectAll);
    }
}
