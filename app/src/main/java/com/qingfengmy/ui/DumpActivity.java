package com.qingfengmy.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.ApplicationAdapter;
import com.qingfengmy.ui.entity.AppInfo;
import com.qingfengmy.ui.utils.tools.ToastUtil;
import com.qingfengmy.ui.view.DampView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/26.
 */
public class DumpActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.dampview)
    DampView dampView;

    @InjectView(R.id.gv_mine)
    GridView gridView_mine;

    @InjectView(R.id.img_mine_bg)
    ImageView img_mine_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dump);
        ButterKnife.inject(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dampView.setImageView(img_mine_bg);

        gridView_mine.setAdapter(new MineAdapter(this));
        gridView_mine.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    class MineAdapter extends BaseAdapter {

        private Context mContext;

        private String[] names = {"笑傲江湖", "书剑恩仇录", "神雕侠侣", "侠客行", "倚天屠龙记", "碧血剑", "鸳鸯刀"};

        public MineAdapter(Context mContext
        ) {
            super();
            this.mContext = mContext;
        }

        public int getCount() {
            return names.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                return convertView;
            }

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_gird, null);
            ImageView imgItem = (ImageView) v.findViewById(R.id.iv_griditem);
            TextView tvItem = (TextView) v.findViewById(R.id.tv_griditem);
            tvItem.setText(names[position]);
            imgItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(mContext, names[position]);
                }
            });
            return v;
        }
    }
}
