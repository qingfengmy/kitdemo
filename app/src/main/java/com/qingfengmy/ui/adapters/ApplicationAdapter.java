package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 *
 *
 */
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    private List<AppInfo> applications;
    private Context context;
    private LayoutInflater mInflater;

    public ApplicationAdapter(Context context, List<AppInfo> applications) {
        this.context = context;
        this.applications = applications;
        mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.row_application, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.cardview)
        CardView card;

        @InjectView(R.id.countryImage)
        ImageView image;

        @InjectView(R.id.countryName)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
