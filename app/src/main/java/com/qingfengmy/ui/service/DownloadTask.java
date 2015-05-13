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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aspsine on 2015/4/20.
 */
public class DownloadTask {
    private Context mContext;
    private FileInfo mFileInfo;

    private int mFinished = 0;
    private boolean mIsPause = false;
    LocalBroadcastManager mLocalBroadcastManager;
    private int threadCount;
    private List<DownloadThread> threadList;
    // 定義線程池
    public static ExecutorService executorService = Executors.newCachedThreadPool();


    public DownloadTask(Context context, FileInfo fileInfo, int threadCount) {
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
        this.threadCount = threadCount;
    }

    public void download() {
        mIsPause = false;
        List<ThreadInfo> threadInfos = new ThreadInfo().getThreadInfos(mFileInfo.getUrl());
        if (threadInfos.size() == 0) {
            // 获得每个线程下载长度
            int length = mFileInfo.getLength() / threadCount;
            for (int i = 0; i < threadCount; i++) {
                // 创建线程信息
                ThreadInfo threadInfo = new ThreadInfo(mFileInfo.getUrl(), length * i, (i + 1) * length - 1, 0);
                if (i == threadCount - 1) {
                    threadInfo.setEnd(mFileInfo.getLength());
                }
                // 添加到线程列表
                threadInfo.save();
                threadInfos.add(threadInfo);
            }
        }
        threadList = new ArrayList<>();

        Intent mIntent = new Intent(Constants.Action.ACTION_START.toString());
        mFileInfo.setStatue(FileInfo.start);
        mIntent.putExtra(DownloadService.EXTRA_FILE_INFO, mFileInfo);
        mLocalBroadcastManager.sendBroadcast(mIntent);

        // 启动多个线程进行下载
        for (ThreadInfo info : threadInfos) {
            DownloadThread thread = new DownloadThread(info);
            DownloadTask.executorService.execute(thread);
            threadList.add(thread);
        }
    }

    public void pause() {
        mIsPause = true;
    }

    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;
        private Intent mIntent;
        public boolean isFinished;

        public DownloadThread(ThreadInfo threadInfo) {
            mThreadInfo = threadInfo;
            this.mIntent = new Intent(Constants.Action.ACTION_UPDATE.toString());
        }

        @Override
        public void run() {
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

                mFinished = mFileInfo.getFinshed();

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
                        // 累加整个文件完成进度
                        mFinished += len;
                        // 累加每个线程的完成进度
                        mThreadInfo.setFinshed(mThreadInfo.getFinshed() + len);

                        // 把进度发送给activity
                        int progress = (int) (mFinished * 100.00 / mFileInfo.getLength());
                        if (tempProgress != progress) {
                            tempProgress = progress;
                            mIntent.putExtra(DownloadService.EXTRA_FINISHED, progress);
                            mIntent.putExtra(DownloadService.EXTRA_ID, mFileInfo.getfId());
                            mLocalBroadcastManager.sendBroadcast(mIntent);
                        }
                        // 暂停保存进度
                        if (mIsPause) {
                            mThreadInfo.update(mThreadInfo.getUrl(), mThreadInfo.getFinshed());
                            mFileInfo.update(mFileInfo.getUrl(), mFinished, FileInfo.pause);
                            Intent mIntent = new Intent(Constants.Action.ACTION_PAUSE.toString());
                            mFileInfo.setStatue(FileInfo.pause);
                            mIntent.putExtra(DownloadService.EXTRA_FILE_INFO, mFileInfo);
                            mLocalBroadcastManager.sendBroadcast(mIntent);
                            return;
                        }
                    }
                    isFinished = true;
                    // 下载完成，删除线程信息
                    mThreadInfo.delete(mThreadInfo.getUrl());
                    // 檢查下載是否完成
                    checkAllThreadFinished();
                }

            } catch (IOException e) {
                e.printStackTrace();
                mIntent.putExtra(DownloadService.EXTRA_FINISHED, -1);
                mLocalBroadcastManager.sendBroadcast(mIntent);
                mThreadInfo.delete(mFileInfo.getUrl());
                mFileInfo.delete();
                Intent mIntent = new Intent(Constants.Action.ACTION_FAILED.toString());
                mFileInfo.setStatue(FileInfo.start);
                mIntent.putExtra(DownloadService.EXTRA_FILE_INFO, mFileInfo);
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

    private synchronized void checkAllThreadFinished() {
        boolean allFinished = true;
        for (DownloadThread thread : threadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        // 發送廣播通知下載任務結束
        if (allFinished) {
            // 下載完成根據url刪除數據庫中的threadInfo
            new ThreadInfo().delete(mFileInfo.getUrl());
            Intent mIntent = new Intent(Constants.Action.ACTION_FINISHED.toString());
            mIntent.putExtra(DownloadService.EXTRA_FILE_INFO, mFileInfo);
            mLocalBroadcastManager.sendBroadcast(mIntent);
            mFileInfo.update(mFileInfo.getUrl(), mFinished, FileInfo.finished);
        }
    }

}
