package com.qingfengmy.ui.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.qingfengmy.ui.entity.FileInfo;
import com.qingfengmy.ui.entity.ThreadInfo;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.FileUtils;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Aspsine on 2015/4/20.
 */
public class DownloadTask {
    private Context mContext;
    private FileInfo mFileInfo;

    private int mFinished = 0;
    private boolean mIsPause = false;
    LocalBroadcastManager mLocalBroadcastManager;


    public DownloadTask(Context context, FileInfo fileInfo) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    public void download() {
        mIsPause = false;
        List<ThreadInfo> threadInfos = new ThreadInfo().getThreadInfos(mFileInfo.getUrl());
        ThreadInfo threadInfo = null;
        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo(mFileInfo.getUrl(), 0, mFileInfo.getLength(), 0);
        } else {
            threadInfo = threadInfos.get(0);
        }
        new DownloadThread(threadInfo).start();
    }

    public void pause() {
        mIsPause = true;
    }

    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;
        private Intent mIntent;

        public DownloadThread(ThreadInfo threadInfo) {
            mThreadInfo = threadInfo;
            this.mIntent = new Intent(Constants.Action.ACTION_UPDATE.toString());
        }

        @Override
        public void run() {
            // 向数据库插入一条下载信息
            if (!mThreadInfo.exists(mThreadInfo.getUrl())) {
                mThreadInfo.save();
            }

            HttpURLConnection httpConn = null;
            InputStream inputStream = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setConnectTimeout(5 * 1000);
                httpConn.setRequestMethod("GET");
                // 设置下载位置
                int start = mThreadInfo.getStart() + mThreadInfo.getFinshed();
                int end = mThreadInfo.getEnd();
                httpConn.setRequestProperty("Range", "bytes=" + start + "-" + end);
                // 设置文件写入位置
                File file = new File(FileUtils.getDownloadDir(mContext), mFileInfo.getName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);

                mFinished = mThreadInfo.getFinshed();

                // 开始下载，部分下载http_partial；全部下载http_ok
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                    inputStream = new BufferedInputStream(httpConn.getInputStream());
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    int tempProgress = 0;
                    // 读取数据
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 写入数据
                        raf.write(buffer, 0, len);
                        mFinished += len;
                        // 把进度发送给activity
                        int progress = Integer.valueOf(mFinished * 100 / mFileInfo.getLength());
                        if (tempProgress != progress) {
                            tempProgress = progress;
                            mIntent.putExtra(DownloadService.EXTRA_FINISHED, progress);
                            mLocalBroadcastManager.sendBroadcast(mIntent);
                        }
                        Log.e("aaa", progress + "---");
                        // 暂停保存进度
                        if (mIsPause) {
                            mThreadInfo.update(mThreadInfo.getUrl(), mFinished);
                            return;
                        }
                    }
                    // 下载完成，删除线程信息
                    mThreadInfo.delete(mThreadInfo.getUrl());
                }

            } catch (IOException e) {
                e.printStackTrace();
                mIntent.putExtra(DownloadService.EXTRA_FINISHED, -1);
                mLocalBroadcastManager.sendBroadcast(mIntent);
            } finally {
                httpConn.disconnect();
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (raf != null) {
                        raf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static interface ProgressCallBacks {
        public void onProgress(int progress);
    }
}
