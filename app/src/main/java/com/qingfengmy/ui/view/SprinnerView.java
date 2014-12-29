package com.qingfengmy.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qingfengmy.R;

import java.util.List;

/**
 * User: zhangtao
 * Date: 2014-12-15
 * Time: 10:58
 */
public class SprinnerView extends LinearLayout {

    List<String> titles;
    ListView listView;
    Context context;

    public SprinnerView(Context context, List<String> titles) {
        super(context);
        this.titles = titles;
        this.context = context;
        View localView = LayoutInflater.from(context).inflate(R.layout.single_list, this);
        listView = (ListView) localView.findViewById(R.id.list);

        listView.setAdapter(new SingleAdapter());
    }


    private class SingleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public String getItem(int position) {
            return titles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final SingleView singleView;
            if (convertView == null) {
                singleView = new SingleView(context);
            } else {
                singleView = (SingleView) convertView;
            }
            singleView.setTitle(titles.get(position));

            singleView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (singleListenner != null) {
                        singleListenner.singleClick(position);
                    }
                    listView.setItemChecked(position, true);
                }
            });
            return singleView;
        }
    }

    SingleCallBackListenner singleListenner;

    public void setSingleCallBackListener(SingleCallBackListenner singleCallBackListener) {
        singleListenner = singleCallBackListener;
    }

    public interface SingleCallBackListenner {
        public void singleClick(int position);
    }
}
