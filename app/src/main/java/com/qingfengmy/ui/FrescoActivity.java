package com.qingfengmy.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingfengmy.R;
import com.qingfengmy.ui.view.AutoButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/27.
 */
public class FrescoActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.my_image_view)
    SimpleDraweeView draweeView;
    @InjectView(R.id.listView)
    ListView listView;

    List<Bean> beans = new ArrayList<>();
    NewAdapter newAdapter;
    MyHandler myHandler;
    @InjectView(R.id.autobutton)
    AutoButton autoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);

        ButterKnife.inject(this);
        mToolbar.setTitle(getName(this));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Uri uri = Uri.parse("http://ww4.sinaimg.cn/bmiddle/406ef017jw1eqjdup8baqg209y05b1ky.gif");
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);

        for(int i=0; i<100; i++){
            Bean bean = new Bean("name"+i, 7200+i*10);
            beans.add(bean);
        }
        newAdapter = new NewAdapter();
        listView.setAdapter(newAdapter);

        myHandler = new MyHandler(beans, newAdapter);
        myHandler.sendEmptyMessageDelayed(0, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeMessages(0);
    }

    private class NewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return beans.size();
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
            ViewHolder holder;
            if(convertView == null){
                convertView = View.inflate(FrescoActivity.this, android.R.layout.simple_list_item_1, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(periodToString(beans.get(position).getTime()*1000l));

            return convertView;
        }
    }

    static class Bean {
        String name;
        int time;

        Bean(String name, int time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    static class ViewHolder{
        @InjectView(android.R.id.text1)
        TextView text;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }

    private String periodToString(Long millisecond) {
        StringBuilder sb = new StringBuilder();
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        long second = millisecond % 86400000 % 3600000 % 60000/1000;
        if (day >= 0) {
            sb.append(String.valueOf(day) + "天");
        }
        if (hour >= 0) {
            sb.append(String.valueOf(hour) + "小时");
        }
        if (minute >= 0) {
            sb.append(String.valueOf(minute) + "分钟");
        }
        if (second >= 0) {
            sb.append(String.valueOf(second) + "秒");
        }
        return sb.toString();
    }

    private static class MyHandler extends Handler {

        private List<Bean> beans;
        private NewAdapter newAdapter;

        private MyHandler(List<Bean> beans, NewAdapter newAdapter) {
            this.beans = beans;
            this.newAdapter = newAdapter;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long begin = System.currentTimeMillis();
            for(int i=0; i<beans.size(); i++){
                int second = beans.get(i).getTime()-1;
                if(second > 0){
                    // 继续
                    beans.get(i).setTime(second);
                }else{
                    // 停止倒计时
                    beans.get(i).setTime(0);
                }
            }
            newAdapter.notifyDataSetChanged();
            sendEmptyMessageDelayed(0,1000);
            long time = System.currentTimeMillis()-begin;
//            Log.e("aaa","间隔时间="+time);
        }
    }
}
