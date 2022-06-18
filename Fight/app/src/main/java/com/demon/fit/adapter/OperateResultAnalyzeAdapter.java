package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateResultAnalyzeAdapter extends RecyclerView.Adapter<OperateResultAnalyzeAdapter.AnalyzeHolder> {

    private Context context;
    private List<OperateTodayBean> beanList;

    // 1.修改对象
    public OperateResultAnalyzeAdapter(Context context, List<OperateTodayBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AnalyzeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_result_analyze, viewGroup, false);
        return new AnalyzeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyzeHolder holder, final int i) {
        // 3.设置界面数据
        OperateTodayBean bean = beanList.get(i);

        holder.tvTime.setText(StringUtil.getDay(bean.createTime));
        holder.tvName.setText(bean.name);
        if (bean.isFollow) {
            holder.tvIsFollow.setText("跟随");
        } else {
            holder.tvIsFollow.setText("");
        }
        holder.tvResult.setText(bean.result + "");

        // 标注 糟糕的操作
        if (bean.isBadOperate) {
            holder.tvIsBad.setText("糟糕");
        } else {
            holder.tvIsBad.setText("");
        }
        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        }

        if (listener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                listener.onItemClick(bean);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AnalyzeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_is_follow)
        TextView tvIsFollow;
        @BindView(R.id.tv_result)
        TextView tvResult;
        @BindView(R.id.tv_is_bad)
        TextView tvIsBad;


        public AnalyzeHolder(@NonNull View view) {
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
