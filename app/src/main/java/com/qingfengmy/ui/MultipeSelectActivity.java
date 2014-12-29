package com.qingfengmy.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.MultipeSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * User: zhangtao
 * Date: 2014-12-29
 * Time: 15:45
 */
public class MultipeSelectActivity extends BaseActivity {
    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.select_all_btn)
    ImageView selectAllBtn;

    List<String> lists;

    int visibleLastIndex;
    MultipeSelectAdapter mAdapter;

    private boolean selectAll;

    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipeselect);
        ButterKnife.inject(this);
        // 先设置title，再设置action，否则无效
        titleBar.setTitle(names[7]);
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // 设置多选模式
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setMultiChoiceModeListener(new ModeCallback());

        // 添加单击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, !listView.isItemChecked(position));

                Toast.makeText(MultipeSelectActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });

        // 添加滑动监听，包括下一页的数据也要全选
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = mAdapter.getCount(); // 数据集最后一项的索引
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && visibleLastIndex == itemsLastIndex) {
                    // 如果是自动加载,可以在这里放置异步加载数据的代码
                    int startposition = lists.size();
                    lists.add("新加的---啊");
                    for (int i = startposition - 1; i < listView.getCount(); i++) {
                        listView.setItemChecked(i, selectAll);
                    }
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount;
            }
        });

        lists = new ArrayList<String>();
        for (int i = 0; i < 15; i++) {
            lists.add("索然啊--" + i);
        }
        // 设置选择单个item的监听事件
        mAdapter = new MultipeSelectAdapter(this, lists, listView);
        mAdapter.setSelectAllListenner(new MultipeSelectAdapter.SelectAllListenner() {
            @Override
            public void clickSelectAll(boolean selectAll) {
                MultipeSelectActivity.this.selectAll = selectAll;
                selectAllBtn.setSelected(MultipeSelectActivity.this.selectAll);
                mAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(mAdapter);

    }

    @OnClick(R.id.layout)
    public void selectAll() {
        selectAll = !selectAll;
        selectAllBtn.setSelected(selectAll);
        // 修改全选或全不选
        for (int i = 0; i < listView.getCount(); i++) {
            listView.setItemChecked(i, selectAll);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class ModeCallback implements ListView.MultiChoiceModeListener {

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }
}
