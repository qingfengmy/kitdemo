package com.qingfengmy.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.TabHostActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TabHostFragment extends Fragment {

    private LayoutInflater inflater;
    private TabHostActivity.StartShake shake;
    private String name;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle bundle = getArguments();
        shake = (TabHostActivity.StartShake) bundle.get("shake");
        name = bundle.getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = inflater.inflate(R.layout.fragment_tabhost, container,
                false);
        ListView listView = (ListView) view
                .findViewById(R.id.fragment1_tabhost_listview);
        listView.setAdapter(new Adapter());
        return view;
    }

    public class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.item_tabhost_fragment,
                        null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            }

            holder.img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    shake.startShake(holder.img);
                }
            });
            holder.name.setText(name + "--恭喜发财");
            return convertView;
        }
    }

    static class ViewHolder {

        @InjectView(R.id.item_tabhost_fragment_img)
        ImageView img;

        @InjectView(R.id.item_tabhost_fragment_name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
