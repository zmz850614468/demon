package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.CompareResultBean;
import com.demon.fit.bean.OperateTodayBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompareOperateResultAdapter extends RecyclerView.Adapter<CompareOperateResultAdapter.CompareHolder> {

    private Context context;
    private List<CompareResultBean> beanList;

    // 1.修改对象
    public CompareOperateResultAdapter(Context context, List<CompareResultBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public CompareHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_compare_operate_result, viewGroup, false);
        return new CompareHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompareHolder holder, final int i) {
        // 3.设置界面数据
        CompareResultBean bean = beanList.get(i);

        holder.tvName.setText(bean.name);
        holder.tvPosCount.setText("盈/次:" + bean.posCount);
        holder.tvNegCount.setText("亏/次:" + bean.negCount);
        holder.tvOperateCount.setText("总/次:" + bean.totalCount);
        holder.tvBadCount.setText("糟糕/次:" + bean.badCount);
        holder.tvOperateResult.setText("操作结果:" + bean.result);
        holder.tvBadPercent.setText(String.format("糟糕占比:%.2f", bean.badPercent) + "%");
        holder.tvPercent.setText(String.format("操作胜率:%.2f", bean.percent) + "%");
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class CompareHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        public TextView tvName;
        @BindView(R.id.tv_pos_count)
        public TextView tvPosCount;
        @BindView(R.id.tv_neg_count)
        public TextView tvNegCount;
        @BindView(R.id.tv_operate_count)
        public TextView tvOperateCount;
        @BindView(R.id.tv_bad_count)
        public TextView tvBadCount;
        @BindView(R.id.tv_operate_result)
        public TextView tvOperateResult;
        @BindView(R.id.tv_bad_percent)
        public TextView tvBadPercent;
        @BindView(R.id.tv_percent)
        public TextView tvPercent;

        public CompareHolder(@NonNull View view) {
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
        public void onItemClick(OperateTodayBean bean);
    }
}
