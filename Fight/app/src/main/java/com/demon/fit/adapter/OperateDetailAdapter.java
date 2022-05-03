package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateDetailAdapter extends RecyclerView.Adapter<OperateDetailAdapter.DetailHolder> {

    private Context context;
    private List<OperateResultBean> beanList;

    // 1.修改对象
    public OperateDetailAdapter(Context context, List<OperateResultBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_detail, viewGroup, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, final int i) {
        // 3.设置界面数据
        OperateResultBean bean = beanList.get(i);

        holder.tvTime.setText(StringUtil.getDay(bean.timeStamp));
        holder.tvResult.setText("" + bean.result);
        holder.tvPoundage.setText("" + bean.poundage);
        holder.tvPosCount.setText("" + bean.posCount);
        holder.tvNegCount.setText("" + bean.negCount);

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        if (listener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(bean);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class DetailHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_time)
        public TextView tvTime;
        @BindView(R.id.tv_result)
        public TextView tvResult;
        @BindView(R.id.tv_poundage)
        public TextView tvPoundage;
        @BindView(R.id.tv_pos_count)
        public TextView tvPosCount;
        @BindView(R.id.tv_neg_count)
        public TextView tvNegCount;

        public DetailHolder(@NonNull View view) {
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
        public void onLongClick(OperateResultBean bean);
    }
}

