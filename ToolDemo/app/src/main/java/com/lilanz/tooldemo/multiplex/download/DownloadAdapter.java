package com.lilanz.tooldemo.multiplex.download;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lilanz.tooldemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadHolder> {

    private Context context;
    private List<DownloadBean> beanList;

    // 1.修改对象
    public DownloadAdapter(Context context, List<DownloadBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public DownloadHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_download, viewGroup, false);
        return new DownloadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadHolder holder, final int i) {
        // 3.设置界面数据
        DownloadBean bean = beanList.get(i);

//        holder.tvOrder.setText(bean.order + "");
        holder.progressBar.setMax(bean.maxProgress);
        holder.progressBar.setProgress(bean.progress);
        holder.tvPercent.setText(bean.percent);

//        if (i % 2 == 0) {
//            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
//        } else {
//            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
//        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class DownloadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.progress)
        ProgressBar progressBar;
        @BindView(R.id.tv_percent)
        TextView tvPercent;

        public DownloadHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(DownloadBean bean);
    }
}

