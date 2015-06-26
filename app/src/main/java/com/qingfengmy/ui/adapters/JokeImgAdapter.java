package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingfengmy.R;
import com.qingfengmy.ui.network.entities.Joke;
import com.qingfengmy.ui.utils.tools.TimeUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/1/21.
 */
public class JokeImgAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Joke> jokeList;

    public JokeImgAdapter(Context context, List<Joke> jokeList) {
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
        if (TextUtils.isEmpty(joke.getImage())) {
            holder.draweeView.setVisibility(View.GONE);
        } else {
            if (joke.getImage().endsWith(".gif")) {
                sb.append("(gif)");
            }

            String a = "";
            String b = "";
            String c = joke.getImage();
            if (c.startsWith("app")) {
                b = c.substring(3, c.indexOf("."));
            } else {
                b = c.substring(0, c.indexOf("."));
            }
            a = b.substring(0, 5);
            String imageUrl = String.format(mContext.getString(R.string.image_url), a, b, c);
            Uri uri = Uri.parse(imageUrl);

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();
            holder.draweeView.setAspectRatio(1.33f);
            holder.draweeView.setController(controller);
        }
        holder.content.setText(sb.append(joke.getContent()).toString());
        holder.time.setText(TimeUtil.formatTime(joke.getTime()));
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