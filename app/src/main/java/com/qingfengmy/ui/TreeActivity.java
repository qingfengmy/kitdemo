package com.qingfengmy.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.SimpleTreeListViewAdapter;
import com.qingfengmy.ui.entity.FileBean;
import com.qingfengmy.ui.utils.Node;
import com.qingfengmy.ui.utils.TreeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/3/4.
 */
public class TreeActivity extends BaseActivity {
    @InjectView(R.id.id_listview)
    ListView mTree;
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;
    private List<FileBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        ButterKnife.inject(this);

        titleBar.setTitle(getName(this));
        setSupportActionBar(titleBar);
        titleBar.setNavigationIcon(R.drawable.ic_menu_back);
        titleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initDatas();
        try
        {
            mAdapter = new SimpleTreeListViewAdapter<FileBean>(mTree, this,
                    mDatas, 0);
            mTree.setAdapter(mAdapter);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        initEvent();
    }

    private void initEvent()
    {
        mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
        {
            @Override
            public void onClick(Node node, int position)
            {
                if (node.isLeaf())
                {
                    Toast.makeText(TreeActivity.this, node.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id)
            {
                // DialogFragment
                final EditText et = new EditText(TreeActivity.this);
                new AlertDialog.Builder(TreeActivity.this).setTitle("Add Node")
                        .setView(et)
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                if (TextUtils.isEmpty(et.getText().toString()))
                                    return;
                                mAdapter.addExtraNode(position, et.getText()
                                        .toString());
                            }
                        }).setNegativeButton("Cancel", null).show();

                return true;
            }
        });
    }

    private void initDatas()
    {
        mDatas = new ArrayList<FileBean>();
        FileBean bean = new FileBean(1, 0, "根目录1");
        mDatas.add(bean);
        bean = new FileBean(2, 0, "根目录2");
        mDatas.add(bean);
        bean = new FileBean(3, 0, "根目录3");
        mDatas.add(bean);
        bean = new FileBean(4, 1, "根目录1-1");
        mDatas.add(bean);
        bean = new FileBean(5, 1, "根目录1-2");
        mDatas.add(bean);
        bean = new FileBean(6, 5, "根目录1-2-1");
        mDatas.add(bean);
        bean = new FileBean(7, 3, "根目录3-1");
        mDatas.add(bean);
        bean = new FileBean(8, 3, "根目录3-2");
        mDatas.add(bean);

        // initDatas
        mDatas = new ArrayList<FileBean>();
        FileBean bean2 = new FileBean(1, 0, "根目录1");
        mDatas.add(bean2);
        bean2 = new FileBean(2, 0, "根目录2");
        mDatas.add(bean2);
        bean2 = new FileBean(3, 0, "根目录3");
        mDatas.add(bean2);
        bean2 = new FileBean(4, 1, "根目录1-1");
        mDatas.add(bean2);
        bean2 = new FileBean(5, 1, "根目录1-2");
        mDatas.add(bean2);
        bean2 = new FileBean(6, 5, "根目录1-2-1");
        mDatas.add(bean2);
        bean2 = new FileBean(7, 3, "根目录3-1");
        mDatas.add(bean2);
        bean2 = new FileBean(8, 3, "根目录3-2");
        mDatas.add(bean2);

    }
}
