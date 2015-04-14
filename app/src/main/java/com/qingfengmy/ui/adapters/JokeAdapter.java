package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.zxing.common.StringUtils;
import com.qingfengmy.R;
import com.qingfengmy.ui.GifActivity;
import com.qingfengmy.ui.network.entities.Joke;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.URI;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Joke> jokeList;

    private Bitmap bmp;

    public JokeAdapter(Context context, List<Joke> jokeList) {
        this.mContext = context;
        this.jokeList = jokeList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return jokeList.size();
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
            convertView = mInflater.inflate(R.layout.item_jokelist,
                    null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        }

        Joke joke = jokeList.get(position);
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(joke.getUrl())) {
            holder.draweeView.setVisibility(View.GONE);
        } else {
            if(joke.getUrl().endsWith(".gif")){
                sb.append("(gif)");
            }
            Uri uri = Uri.parse(joke.getUrl());
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.draweeView.setAspectRatio(1.33f);
            holder.draweeView.setController(controller);
        }
        holder.content.setText(sb.append(joke.getContent()).toString());
        holder.time.setText(joke.getUpdatetime());
        return convertView;
    }


    public static class ViewHolder {

        @InjectView(R.id.textView)
        TextView content;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.img)
        SimpleDraweeView draweeView;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void addJokes(List<Joke> jokes) {
        jokeList.addAll(jokes);
        notifyDataSetChanged();
    }
}
