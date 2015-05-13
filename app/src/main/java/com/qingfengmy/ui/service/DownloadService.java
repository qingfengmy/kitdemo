package com.qingfengmy.ui.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.qingfengmy.ui.entity.FileInfo;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by aspsine on 15-4-19.
 */
public class DownloadService extends Service {
    private static final String TAG = DownloadService.class.getSimpleName();
    public static final String EXTRA_FILE_INFO = "file_info";
    public static final String EXTRA_FINISHED = "finished";
    public static final String EXTRA_ID = "fid";

    private static final int MSG_INIT = 0;

    private DownloadTask mDownloadTask;
    LocalBroadcastManager mLocalBroadcastManager;

    private Map<Integer, DownloadTask> mTask = new LinkedHashMap<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_INIT) {
                FileInfo fileInfo = (FileInfo) msg.obj;
                DownloadTask mDownloadTask = new DownloadTask(DownloadService.this, fileInfo,3);
                mDownloadTask.download();
                mTask.put(fileInfo.getfId(), mDownloadTask);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * onStart已经在2.0被废弃
     * onStartCommand是替代品，第一次startService会执行oncreate-onstartCommand
     * 之后再执行startService就只执行onstartCommand，所以多数逻辑处理都在这里
     * 如果bindservice，可以在activity中调用service方法，准确的说是通过IBinder调用service方法
     * 绑定后，记住在activity的ondestroy时解绑。
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null){
            return super.onStartCommand(intent, flags, startId);
        }
        String action = intent.getAction();
        if (Constants.Action.ACTION_START.toString().equals(action)) {
            // 開始
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(EXTRA_FILE_INFO);
            download(fileInfo);
        } else if (Constants.Action.ACTION_PAUSE.toString().equals(action)) {
            // 暫停
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(EXTRA_FILE_INFO);
            DownloadTask task = mTask.get(fileInfo.getfId());
            if (task != null)
                task.pause();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void download(FileInfo fileInfo) {
        DownloadTask.executorService.execute(new InitThread(this, fileInfo));
    }

    /**
     * 初始化线程是获取网络文件长度，并建立同样大小的本地文件
     */
    class InitThread extends Thread {
        FileInfo mFileInfo;
        Context mContext;
        private Intent mIntent;

        private InitThread(Context context, FileInfo fileInfo) {
            mFileInfo = fileInfo;
            mContext = context;
            this.mIntent = new Intent(Constants.Action.ACTION_UPDATE.toString());
        }

        @Override
        public void run() {
            HttpURLConnection httpConn = null;
            RandomAccessFile raf = null;
            try {
                // 链接网络文件
                URL url = new URL(mFileInfo.getUrl());
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setConnectTimeout(5 * 1000);
                // 获得文件长度
                int length = -1;
                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    length = httpConn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                // 建立本地文件
                File dir = FileUtils.getDownloadDir(mContext);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, mFileInfo.getName());
                /**
                 * model各个参数详解
                 * r 代表以只读方式打开指定文件
                 * rw 以读写方式打开指定文件
                 * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
                 * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
                 *
                 * **/
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                mFileInfo.setLength(length);

               FileInfo tempInfo = mFileInfo.existFileInfo(mFileInfo.getUrl());
                if(tempInfo == null){
                    mFileInfo.save();
                }else{
                    mFileInfo.setFinshed(tempInfo.getFinshed());
                }

                handler.obtainMessage(MSG_INIT, mFileInfo).sendToTarget();
            } catch (IOException e) {
                e.printStackTrace();
                mIntent.putExtra(DownloadService.EXTRA_FINISHED, -1);
                mLocalBroadcastManager.sendBroadcast(mIntent);
                Log.e("aaa", "e="+e.toString());
            } finally {
                httpConn.disconnect();
                try {
                    if (raf != null) raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
