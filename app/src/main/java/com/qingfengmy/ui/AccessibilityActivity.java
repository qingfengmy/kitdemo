package com.qingfengmy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingfengmy.R;

import java.util.ArrayList;
import java.util.List;

public class AccessibilityActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Integer> demos = new ArrayList<>();
    DemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 到无障碍设置界面
                Intent localIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(localIntent);
            }
        });

        for (int i=0 ;i<15; i++){
            demos.add(i);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(this));
        recyclerView.setHasFixedSize(true);
        adapter = new DemoAdapter(demos);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accessibility, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            demos.add(demos.size());
            adapter.notifyDataSetChanged();
            return true;
        }else if(id == R.id.action_remove){
            demos.remove(demos.size()-1);
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class MarginDecoration extends RecyclerView.ItemDecoration {
        private int margin;

        public MarginDecoration(Context context) {
            margin = context.getResources().getDimensionPixelSize(R.dimen.item_margin);
        }

        @Override
        public void getItemOffsets(
                Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(margin, margin, margin, margin);
        }
    }

    static class DemoAdapter extends RecyclerView.Adapter<TextViewHolder> {
        private final List<Integer> demos;

        public DemoAdapter(List<Integer> demos) {
            this.demos = demos;
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accessibility, parent, false);
            return new TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TextViewHolder holder, final int position) {
            final int demo = demos.get(position);
            holder.textView.setText(demo + "");
        }

        @Override
        public int getItemCount() {
            return demos.size();
        }
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

}
