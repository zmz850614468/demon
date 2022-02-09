package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.CostBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.CostHolder> {

    private Context context;
    private List<CostBean> beanList;

    // 1.修改对象
    public CostAdapter(Context context, List<CostBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public CostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_cost, viewGroup, false);
        return new CostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CostHolder holder, final int i) {
        // 3.设置界面数据
        CostBean bean = beanList.get(i);

        holder.tvOrder.setText(bean.index + "");
        holder.tvName.setText(bean.name);
        holder.tvCost.setText(bean.cost + "");
        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(bean));
//            holder.itemView.setOnLongClickListener(v -> {
//                listener.onLongClick(bean);
//                return false;
//            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class CostHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_cost)
        TextView tvCost;


        public CostHolder(@NonNull View view) {
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
        void onItemClick(CostBean bean);

        void onLongClick(CostBean bean);
    }
}
