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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.entity.FileInfo;
import com.qingfengmy.ui.service.DownloadService;
import com.qingfengmy.ui.service.DownloadTask;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/4/22.
 */
public class MultiThreadDownloadActivity extends BaseActivity implements DownloadTask.ProgressCallBacks{
    public static final String DOWNLOAD_URL = "http://www.imooc.com/mobile/imooc.apk";
    @InjectView(R.id.toolbar)
    Toolbar titleBar;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    @InjectView(R.id.tvProgress)
    TextView tvProgress;

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

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intent = new IntentFilter();
        intent.addAction(Constants.Action.ACTION_UPDATE.toString());
        mReceiver = new DownloadProgressReceiver();
        mReceiver.setProgressCallBacks(this);
        mLocalBroadcastManager.registerReceiver(mReceiver, intent);

        this.pause = true;
    }

    int p;
    boolean pause;
    @Override
    public void onProgress(int progress) {
        if(progress == -1){
            ToastUtil.showToast(this, "网络超时");
            this.pause = true;
            return;
        }
        progressBar.setProgress(progress);
        p = progress;
        tvProgress.setText(progress + "%");
        if (p == 100){
            ToastUtil.showToast(this, "下载完成");
            this.pause = true;
        }

    }


    private static class DownloadProgressReceiver extends BroadcastReceiver {
        DownloadTask.ProgressCallBacks mCallBacks;

        void setProgressCallBacks(DownloadTask.ProgressCallBacks callBacks) {
            this.mCallBacks = callBacks;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int finished = intent.getIntExtra(DownloadService.EXTRA_FINISHED, 0);
            mCallBacks.onProgress(finished);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    @OnClick(R.id.btnStart)
    public void start() {
        if (pause){
            FileInfo fileInfo = new FileInfo();
            fileInfo.setUrl(DOWNLOAD_URL);
            fileInfo.setName("daily.apk");
            Intent intent = new Intent(this, DownloadService.class);
            intent.setAction(Constants.Action.ACTION_START.toString());
            intent.putExtra(DownloadService.EXTRA_FILE_INFO, fileInfo);
            tvProgress.setText(p + "%");
            startService(intent);
            pause = false;
        }else{
            ToastUtil.showToast(this, "已经在下载了");
        }
    }

    @OnClick(R.id.btnPause)
    public void pause() {
        if (pause){
            ToastUtil.showToast(this, "已经暂停了");
            return;
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(DOWNLOAD_URL);
        fileInfo.setName("daily.apk");
        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(Constants.Action.ACTION_PAUSE.toString());
        intent.putExtra(DownloadService.EXTRA_FILE_INFO, fileInfo);
        tvProgress.setText("暂停");
        startService(intent);
        pause = true;
    }
}
