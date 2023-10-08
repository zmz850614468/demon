package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.FuLiBean;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FuLiAdapter extends RecyclerView.Adapter<FuLiAdapter.FuLiHolder> {


    private Context context;
    private List<FuLiBean> beanList;

    // 1.修改对象
    public FuLiAdapter(Context context, List<FuLiBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public FuLiHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_ful, viewGroup, false);
        return new FuLiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuLiHolder holder, final int i) {
        // 3.设置界面数据
        FuLiBean bean = beanList.get(i);

        if (StringUtil.isEmpty(bean.date)) {
            holder.tvOrder.setText((i + 1) + "");
        } else {
            holder.tvOrder.setText(bean.date);
        }

        holder.tvBase.setText(bean.base + "");
        holder.tvTimes.setText(bean.times + "");
        holder.tvFul.setText(String.format("%.1f", (bean.fuL - 1) * 100) + "%");
        holder.tvResult.setText(String.format("%.2f", bean.result));

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        }

        if (listener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                listener.onItemClick(bean);
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class FuLiHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_base)
        TextView tvBase;
        @BindView(R.id.tv_ful)
        TextView tvFul;
        @BindView(R.id.tv_result)
        TextView tvResult;
        @BindView(R.id.tv_times)
        TextView tvTimes;

        public FuLiHolder(@NonNull View view) {
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
        public void onItemClick(FuLiBean bean);
    }
}



























