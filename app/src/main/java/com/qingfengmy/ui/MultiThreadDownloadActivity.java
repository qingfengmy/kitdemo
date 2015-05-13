package com.qingfengmy.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.adapters.MultiThreadDownloadAdapter;
import com.qingfengmy.ui.entity.FileInfo;
import com.qingfengmy.ui.service.DownloadService;
import com.qingfengmy.ui.service.DownloadTask;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/4/22.
 */
public class MultiThreadDownloadActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    Toolbar titleBar;

    @InjectView(R.id.listview)
    ListView listView;
    private List<FileInfo> fileInfoList;
    private MultiThreadDownloadAdapter multiThreadDownloadAdapter;

    DownloadProgressReceiver mReceiver;
    LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multithread);
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

        fileInfoList = new ArrayList<>();
        FileInfo fileInfo1 = new FileInfo(1, "http://gdown.baidu.com/data/wisegame/d5e850fc388599f4/nishinayiweitonghuagongzhu_1.apk", "你是哪位童话公主", 0, 0);
        fileInfo1.setFinshed(fileInfo1.getHasfinished(fileInfo1.getUrl()));
        fileInfo1.setStatue(fileInfo1.getDownloadStatue(fileInfo1.getUrl()));
        FileInfo fileInfo2 = new FileInfo(2, "http://gdown.baidu.com/data/wisegame/8262bdb5f6a99e0f/nishisanguolideshui_1.apk", "你是三国里的谁", 0, 0);
        fileInfo2.setFinshed(fileInfo2.getHasfinished(fileInfo2.getUrl()));
        fileInfo2.setStatue(fileInfo2.getDownloadStatue(fileInfo2.getUrl()));
        FileInfo fileInfo3 = new FileInfo(3, "http://gdown.baidu.com/data/wisegame/216466323c1905f7/qiangpozhengtouxiangzhizuo_1.apk", "强迫症头像神器", 0, 0);
        fileInfo3.setFinshed(fileInfo3.getHasfinished(fileInfo3.getUrl()));
        fileInfo3.setStatue(fileInfo3.getDownloadStatue(fileInfo3.getUrl()));
        FileInfo fileInfo4 = new FileInfo(4, "http://gdown.baidu.com/data/wisegame/606de174fae4d920/meirongyangsheng_1.apk", "养生美容", 0, 0);
        fileInfo4.setFinshed(fileInfo1.getHasfinished(fileInfo4.getUrl()));
        fileInfo4.setStatue(fileInfo1.getDownloadStatue(fileInfo4.getUrl()));
        FileInfo fileInfo5 = new FileInfo(5, "http://gdown.baidu.com/data/wisegame/9f9e673014224af5/weifenxiaotezhao_1.apk", "微商特招", 0, 0);
        fileInfo5.setFinshed(fileInfo1.getHasfinished(fileInfo5.getUrl()));
        fileInfo5.setStatue(fileInfo1.getDownloadStatue(fileInfo5.getUrl()));
        FileInfo fileInfo6 = new FileInfo(6, "http://bcs.91.com/rbreszy/android/soft/2015/2/4/db9bff9396124fe4b2c87c83febdb889/cn.mchina.wfenxiao_1_1.0_635586525910355972.apk", "微分销", 0, 0);
        fileInfo6.setFinshed(fileInfo1.getHasfinished(fileInfo6.getUrl()));
        fileInfo6.setStatue(fileInfo1.getDownloadStatue(fileInfo6.getUrl()));
        FileInfo fileInfo7 = new FileInfo(7, "http://125.39.66.163/files/5218000000CACD89/files2.genymotion.com/genymotion/genymotion-2.4.0/genymotion-2.4.0-vbox.exe", "大家伙", 0, 0);
        fileInfo7.setFinshed(fileInfo1.getHasfinished(fileInfo7.getUrl()));
        fileInfo7.setStatue(fileInfo1.getDownloadStatue(fileInfo7.getUrl()));

        fileInfoList.add(fileInfo1);
        fileInfoList.add(fileInfo2);
        fileInfoList.add(fileInfo3);
        fileInfoList.add(fileInfo4);
        fileInfoList.add(fileInfo5);
        fileInfoList.add(fileInfo6);
        fileInfoList.add(fileInfo7);

        multiThreadDownloadAdapter = new MultiThreadDownloadAdapter(this, fileInfoList);
        listView.setAdapter(multiThreadDownloadAdapter);

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(Constants.Action.ACTION_UPDATE.toString());
        intent.addAction(Constants.Action.ACTION_FINISHED.toString());
        intent.addAction(Constants.Action.ACTION_FAILED.toString());
        intent.addAction(Constants.Action.ACTION_START.toString());
        intent.addAction(Constants.Action.ACTION_PAUSE.toString());
        mReceiver = new DownloadProgressReceiver();
        mLocalBroadcastManager.registerReceiver(mReceiver, intent);

    }


    private class DownloadProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Constants.Action.ACTION_UPDATE.toString())) {
                // 进度更新
                int finished = intent.getIntExtra(DownloadService.EXTRA_FINISHED, 0);
                int fid = intent.getIntExtra(DownloadService.EXTRA_ID, 0);
                multiThreadDownloadAdapter.updateProgress(fid, finished);
            } else if (intent.getAction().equalsIgnoreCase(Constants.Action.ACTION_FINISHED.toString())) {
                // 下载完成
                FileInfo info = (FileInfo) intent.getSerializableExtra(DownloadService.EXTRA_FILE_INFO);
                if (info != null) {
                    multiThreadDownloadAdapter.updateProgress(info.getfId(), 100);
                    ToastUtil.showToast(MultiThreadDownloadActivity.this, info.getName() + "下載完成");
                }
            }else if (intent.getAction().equalsIgnoreCase(Constants.Action.ACTION_FAILED.toString())) {
                // 下载失败
                FileInfo info = (FileInfo) intent.getSerializableExtra(DownloadService.EXTRA_FILE_INFO);
                if (info != null) {
                    multiThreadDownloadAdapter.updateProgress(info.getfId(), -1);
                    ToastUtil.showToast(MultiThreadDownloadActivity.this, info.getName() + "下載失败");
                }
            }else if (intent.getAction().equalsIgnoreCase(Constants.Action.ACTION_START.toString())) {
                // 下载开始
                FileInfo info = (FileInfo) intent.getSerializableExtra(DownloadService.EXTRA_FILE_INFO);
                if (info != null) {
                    multiThreadDownloadAdapter.changeStatue(info.getfId(), info.getStatue());
                }
            }else if (intent.getAction().equalsIgnoreCase(Constants.Action.ACTION_PAUSE.toString())) {
                // 下载暂停
                FileInfo info = (FileInfo) intent.getSerializableExtra(DownloadService.EXTRA_FILE_INFO);
                if (info != null) {
                    multiThreadDownloadAdapter.changeStatue(info.getfId(), info.getStatue());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

}
