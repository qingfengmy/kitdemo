package com.qingfengmy.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qingfengmy.R;
import com.qingfengmy.ui.entity.FileInfo;
import com.qingfengmy.ui.service.DownloadService;
import com.qingfengmy.ui.utils.Constants;
import com.qingfengmy.ui.utils.tools.ToastUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/5/11.
 */
public class MultiThreadDownloadAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private List<FileInfo> fileInfos;

    public MultiThreadDownloadAdapter(Context context, List<FileInfo> fileInfos) {
        this.context = context;
        this.fileInfos = fileInfos;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileInfos.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final FileInfo fileInfo = fileInfos.get(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_multithread, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

            holder.progressBar.setMax(100);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvFileName.setText(fileInfo.getName());
        holder.progressBar.setProgress(fileInfo.getFinshed());
        holder.tvProgress.setText(fileInfo.getFinshed() + "%");

        if (fileInfo.getStatue() == FileInfo.finished) {
            // 下载完成
            holder.btnPause.setEnabled(false);
            holder.btnStart.setEnabled(true);
        } else if (fileInfo.getStatue() == FileInfo.start) {
            // 正在下载
            holder.btnPause.setEnabled(true);
            holder.btnStart.setEnabled(false);
        } else if (fileInfo.getStatue() == FileInfo.pause) {
            // 暂停下载或暂未开始
            holder.btnPause.setEnabled(false);
            holder.btnStart.setEnabled(true);
        }

        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DownloadService.class);
                intent.setAction(Constants.Action.ACTION_START.toString());
                if (fileInfo.getStatue() == FileInfo.finished) {
                    fileInfo.setStatue(FileInfo.start);
                    fileInfo.setFinshed(0);
                    fileInfo.update(fileInfo.getUrl(), fileInfo.getFinshed(), fileInfo.getStatue());
                }
                intent.putExtra(DownloadService.EXTRA_FILE_INFO, fileInfo);
                context.startService(intent);
                holder.btnPause.setEnabled(true);
                holder.btnStart.setEnabled(false);
            }
        });

        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DownloadService.class);
                intent.setAction(Constants.Action.ACTION_PAUSE.toString());
                intent.putExtra(DownloadService.EXTRA_FILE_INFO, fileInfo);
                context.startService(intent);
                holder.btnPause.setEnabled(false);
                holder.btnStart.setEnabled(true);
            }
        });
        return convertView;
    }

    // 更新列表項中的進度條
    public void updateProgress(long fid, int progress) {
        for (int i = 0; i < fileInfos.size(); i++) {
            FileInfo fileInfo = fileInfos.get(i);
            if (fileInfo.getfId() == fid) {
                fileInfo.setFinshed(progress);
                if (progress == 100) {
                    // 下载完成
                    fileInfo.setStatue(FileInfo.finished);
                } else if (progress == -1) {
                    // 下载失败
                    fileInfo.setStatue(FileInfo.pause);
                    fileInfo.setFinshed(0);
                }
                break;
            }
        }
        notifyDataSetChanged();
    }

    // 下载状态改变
    public void changeStatue(long fid, int statue) {
        for (int i = 0; i < fileInfos.size(); i++) {
            FileInfo fileInfo = fileInfos.get(i);
            if (fileInfo.getfId() == fid) {
                fileInfo.setStatue(statue);
                break;
            }
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder {
        @InjectView(R.id.tvFileName)
        TextView tvFileName;

        @InjectView(R.id.tvProgress)
        TextView tvProgress;

        @InjectView(R.id.progressBar)
        ProgressBar progressBar;

        @InjectView(R.id.btnPause)
        Button btnPause;

        @InjectView(R.id.btnStart)
        Button btnStart;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }

}
