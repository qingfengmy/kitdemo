package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.common.StringUtils;
import com.qingfengmy.R;
import com.qingfengmy.ui.GifActivity;
import com.qingfengmy.ui.network.entities.Joke;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

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
        holder.content.setText(joke.getContent());
        holder.time.setText(joke.getUpdatetime());

        if (TextUtils.isEmpty(joke.getUrl())) {
            holder.img.setVisibility(View.GONE);
        } else {
            if (joke.getUrl().endsWith("gif")) {
                holder.gif.setVisibility(View.VISIBLE);

                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bmp != null) {
                            // 浏览gif的界面
                            Intent intent = new Intent();
                            intent.putExtra("bitmap", bmp);
                            intent.setClass(mContext, GifActivity.class);
                            mContext.startActivity(intent);
                        }
                    }
                });
                Picasso.with(mContext).load(joke.getUrl()).placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bmp = bitmap;
                        holder.img.setImageBitmap(bmp);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            } else {
                holder.gif.setVisibility(View.GONE);
                Picasso.with(mContext).load(joke.getUrl()).placeholder(R.drawable.default_image)
                        .error(R.drawable.default_image).into(holder.img);
            }

        }
        return convertView;
    }


    static class ViewHolder {

        @InjectView(R.id.textView)
        TextView content;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.gif)
        TextView gif;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    public void addJokes(List<Joke> jokes) {
        jokeList.addAll(jokes);
        notifyDataSetChanged();
    }
}
