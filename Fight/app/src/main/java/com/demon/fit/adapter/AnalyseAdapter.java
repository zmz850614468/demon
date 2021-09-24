package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.AnalyseBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnalyseAdapter extends RecyclerView.Adapter<AnalyseAdapter.AnalyseHolder> {

    private Context context;
    private List<AnalyseBean> beanList;

    // 1.修改对象
    public AnalyseAdapter(Context context, List<AnalyseBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AnalyseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_analyse, viewGroup, false);
        return new AnalyseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyseHolder holder, final int i) {
        // 3.设置界面数据
        AnalyseBean bean = beanList.get(i);

        holder.tvTime.setText(String.format("%d月", (bean.time) + 1));
        holder.tvOperateTimes.setText(bean.operateTimes + "");
        holder.tvGoodTimes.setText(bean.goodTimes + "");
        holder.tvGoodResult.setText(bean.goodResult + "");
        holder.tvBadTimes.setText(bean.badTimes + "");
        holder.tvBadResult.setText(bean.badResult + "");
        holder.tvTotalResult.setText(bean.totalResult + "");

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AnalyseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_operate_times)
        TextView tvOperateTimes;
        @BindView(R.id.tv_good_times)
        TextView tvGoodTimes;
        @BindView(R.id.tv_good_result)
        TextView tvGoodResult;
        @BindView(R.id.tv_bad_times)
        TextView tvBadTimes;
        @BindView(R.id.tv_bad_result)
        TextView tvBadResult;
        @BindView(R.id.tv_total_result)
        TextView tvTotalResult;

        public AnalyseHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvGoodTimes.setBackgroundResource(R.color.deepRed);
            tvGoodResult.setBackgroundResource(R.color.deepRed);
            tvBadTimes.setBackgroundResource(R.color.deepGreen);
            tvBadResult.setBackgroundResource(R.color.deepGreen);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(AnalyseBean bean);
    }
}
