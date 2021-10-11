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

public class AnalyseV2Adapter extends RecyclerView.Adapter<AnalyseV2Adapter.AnalyseHolder> {

    private Context context;
    private List<AnalyseBean> beanList;

    // 1.修改对象
    public AnalyseV2Adapter(Context context, List<AnalyseBean> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_analyse_v2, viewGroup, false);
        return new AnalyseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyseHolder holder, final int i) {
        // 3.设置界面数据
        AnalyseBean bean = beanList.get(i);

        holder.tvMonth.setText(String.format("%d月", (bean.time) + 1));
        holder.tvGoodTimes.setText(bean.goodTimes + "");
        holder.tvGoodResult.setText(bean.goodResult + "");
        holder.tvGoodRate.setText(String.format("%.1f", (bean.goodTimes * 100.0f / bean.operateTimes)) + "%");

        holder.tvBadTimes.setText(bean.badTimes + "");
        holder.tvBadResult.setText(bean.badResult + "");
        holder.tvBadRate.setText(String.format("%.1f", (bean.badTimes * 100.0f / bean.operateTimes)) + "%");

        if (bean.resultTime == 0) {
            holder.tvPosTimes.setText("--");
            holder.tvPosResult.setText("--");
            holder.tvPosRate.setText("--");
            holder.tvNegTimes.setText("--");
            holder.tvNegResult.setText("--");
            holder.tvNegRate.setText("--");

            holder.tvTotalResult.setText("--");
            holder.tvTotalTimes.setText("--");
        } else {
            holder.tvPosTimes.setText(bean.posTimes + "");
            holder.tvPosResult.setText(bean.posResult + "");
            holder.tvPosRate.setText(String.format("%.1f", (bean.posTimes * 100.0f / bean.resultTime)) + "%");

            holder.tvNegTimes.setText(bean.negTimes + "");
            holder.tvNegResult.setText(bean.negResult + "");
            holder.tvNegRate.setText(String.format("%.1f", (bean.negTimes * 100.0f / bean.resultTime)) + "%");

            holder.tvTotalTimes.setText(bean.resultTime + "");
            holder.tvTotalResult.setText(bean.totalResult + "");
        }
//        holder.tvTime.setText(String.format("%d月", (bean.time) + 1));
//        holder.tvOperateTimes.setText(bean.operateTimes + "");
//        holder.tvGoodTimes.setText(bean.goodTimes + "");
//        holder.tvGoodResult.setText(bean.goodResult + "");
//        holder.tvBadTimes.setText(bean.badTimes + "");
//        holder.tvBadResult.setText(bean.badResult + "");
//        holder.tvTotalResult.setText(bean.totalResult + "");

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

    public class AnalyseHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_good_times)
        TextView tvGoodTimes;
        @BindView(R.id.tv_good_result)
        TextView tvGoodResult;
        @BindView(R.id.tv_good_rate)
        TextView tvGoodRate;
        @BindView(R.id.tv_bad_times)
        TextView tvBadTimes;
        @BindView(R.id.tv_bad_result)
        TextView tvBadResult;
        @BindView(R.id.tv_bad_rate)
        TextView tvBadRate;
        @BindView(R.id.tv_pos_times)
        TextView tvPosTimes;
        @BindView(R.id.tv_pos_result)
        TextView tvPosResult;
        @BindView(R.id.tv_pos_rate)
        TextView tvPosRate;
        @BindView(R.id.tv_neg_times)
        TextView tvNegTimes;
        @BindView(R.id.tv_neg_result)
        TextView tvNegResult;
        @BindView(R.id.tv_neg_rate)
        TextView tvNegRate;
        @BindView(R.id.tv_total_times)
        TextView tvTotalTimes;
        @BindView(R.id.tv_total_result)
        TextView tvTotalResult;
        @BindView(R.id.tv_month)
        TextView tvMonth;

        public AnalyseHolder(@NonNull View view) {
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
        public void onItemClick(AnalyseBean bean);
    }
}
