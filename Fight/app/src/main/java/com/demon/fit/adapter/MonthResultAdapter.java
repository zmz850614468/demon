package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.MonthResultBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthResultAdapter extends RecyclerView.Adapter<MonthResultAdapter.MonthResultHolder> {

    private Context context;
    private List<MonthResultBean> beanList;

    // 1.修改对象
    public MonthResultAdapter(Context context, List<MonthResultBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public MonthResultHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_result, viewGroup, false);
        return new MonthResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthResultHolder holder, final int i) {
        // 3.设置界面数据
        MonthResultBean bean = beanList.get(i);

        holder.tvOperateResult.setText("结果:" + bean.operateResult);
        holder.tvPoundage.setText("手续:" + bean.poundage);
        holder.tvPosCount.setText("正操作:" + bean.posCount);
        holder.tvNegCount.setText("负操作:" + bean.negCount);
        holder.tvTotalCount.setText("总操作:" + bean.operateResult);
        holder.tvPercent.setText(String.format("胜率:%.2f", bean.percent) + "%");
        holder.tvTotalResult.setText("总盈亏:" + bean.totalResult);
        holder.tvMonth.setText((bean.month + 1) + "月");

//            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class MonthResultHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_operate_result)
        TextView tvOperateResult;
        @BindView(R.id.tv_poundage)
        TextView tvPoundage;
        @BindView(R.id.tv_pos_count)
        TextView tvPosCount;
        @BindView(R.id.tv_neg_count)
        TextView tvNegCount;
        @BindView(R.id.tv_operate_count)
        TextView tvTotalCount;
        @BindView(R.id.tv_percent)
        TextView tvPercent;
        @BindView(R.id.tv_total_result)
        TextView tvTotalResult;
        @BindView(R.id.tv_month)
        TextView tvMonth;


        public MonthResultHolder(@NonNull View view) {
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
        public void onItemClick(MonthResultBean bean);
    }
}
