package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.BaseActivity;
import com.qingfengmy.ui.entity.AppInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * User: zhangtao
 * Date: 2014-12-31
 * Time: 11:09
 */
public class ApplicationAdapter extends BaseAdapter {
    private List<AppInfo> applications;
    private Context context;
    private LayoutInflater mInflater;

    public ApplicationAdapter(Context context, List<AppInfo> applications) {
        this.context = context;
        this.applications = applications;
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setApplications(List<AppInfo> applications) {
        this.applications = applications;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return applications.size();
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
            convertView = mInflater.inflate(R.layout.row_application,
                    null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }
        final AppInfo appInfo = applications.get(position);
        holder.name.setText(appInfo.getName());
        holder.image.setImageDrawable(appInfo.getIcon());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               BaseActivity activity = (BaseActivity) context;
                activity.animateActivity(appInfo, holder.image);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.cardview)
        CardView card;

        @InjectView(R.id.countryImage)
        ImageView image;

        @InjectView(R.id.countryName)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
