package com.demon.remotecontrol.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.AppInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListHodler> {

    private Context context;
    private List<AppInfoBean> beanList;

    // 1.修改对象
    public AppListAdapter(Context context, List<AppInfoBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AppListHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_app_list, viewGroup, false);
        return new AppListHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListHodler holder, final int i) {
        // 3.设置界面数据
        AppInfoBean bean = beanList.get(i);
        holder.tvOrder.setText((i + 1) + "");
        holder.tvAppName.setText(bean.appName);
        holder.tvAppPackage.setText(bean.packageName);
        holder.tvAppSize.setText(bean.appSize);

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(bean));
        }

    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AppListHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_app_name)
        TextView tvAppName;
        @BindView(R.id.tv_app_package)
        TextView tvAppPackage;
        @BindView(R.id.tv_app_size)
        TextView tvAppSize;

        public AppListHodler(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件

            tvAppName.setGravity(Gravity.CENTER_VERTICAL);
            tvAppPackage.setGravity(Gravity.CENTER_VERTICAL);
            tvAppSize.setGravity(Gravity.CENTER_VERTICAL);
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(AppInfoBean bean);
    }
}

