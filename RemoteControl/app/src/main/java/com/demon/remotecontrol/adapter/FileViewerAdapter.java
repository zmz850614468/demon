package com.demon.remotecontrol.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.FileInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileViewerAdapter extends RecyclerView.Adapter<FileViewerAdapter.FileViewerHolder> {

    private Context context;
    private List<FileInfoBean> beanList;

    // 1.修改对象
    public FileViewerAdapter(Context context, List<FileInfoBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public FileViewerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_file_viewer, viewGroup, false);
        return new FileViewerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewerHolder holder, final int i) {
        // 3.设置界面数据
        FileInfoBean bean = beanList.get(i);
        holder.tvOrder.setText((i + 1) + "");
        holder.tvFileName.setText(bean.fileName);

        if (bean.isFile) {
            holder.ivFileType.setBackgroundResource(R.mipmap.icon_file);
        } else {
            holder.ivFileType.setBackgroundResource(R.mipmap.icon_file_folder);
        }

        if (listener != null) {
            holder.tvFileName.setOnClickListener(v -> {
                listener.onItemClick(bean);
            });
            holder.tvFileName.setOnLongClickListener(v -> {
                listener.onDeleteCLick(bean);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class FileViewerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.iv_file_type)
        ImageView ivFileType;
        @BindView(R.id.tv_file_name)
        TextView tvFileName;

        public FileViewerHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件

            tvFileName.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(FileInfoBean bean);

        void onDeleteCLick(FileInfoBean bean);
    }
}
