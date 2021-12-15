package com.demon.opencvbase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.demon.opencvbase.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateAdapter extends RecyclerView.Adapter<OperateAdapter.OperateHolder> {

    private Context context;
    private List<String> beanList;

    private int selectIndex = -1;

    // 1.修改对象
    public OperateAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public OperateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate, viewGroup, false);
        return new OperateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperateHolder holder, final int i) {
        // 3.设置界面数据
        final String bean = beanList.get(i);

        holder.tvOrder.setText((i + 1) + "");
        holder.tvOperate.setText(bean);

        if (i == selectIndex) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_red);
        } else if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        }

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectIndex = i;
                    listener.onItemClick(bean);
                    OperateAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class OperateHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_operate)
        TextView tvOperate;

        public OperateHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件
        }
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(String bean);
    }
}



