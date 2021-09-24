package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.ResultBean;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ReshltHodler> {

    private Context context;
    private List<ResultBean> beanList;

    // 1.修改对象
    public ResultAdapter(Context context, List<ResultBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ReshltHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, viewGroup, false);
        return new ReshltHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReshltHodler holder, final int i) {
        // 3.设置界面数据
        ResultBean bean = beanList.get(beanList.size() - 1 - i);

        holder.tvTime.setText(StringUtil.getDay(bean.timeStamp));
        holder.tvName.setText(bean.name);
        holder.tvType.setText(bean.type);
//        if ("入手".equals(bean.type)) {
//            holder.tvType.setTextColor(context.getResources().getColor(R.color.blue));
//        }else {
//            holder.tvType.setTextColor(context.getResources().getColor(R.color.red));
//        }

        holder.tvIsRight.setText(bean.isRight ? "好" : "坏");
//        if (bean.isRight) {
//            holder.tvIsRight.setTextColor(context.getResources().getColor(R.color.red));
//        }else {
//            holder.tvIsRight.setTextColor(context.getResources().getColor(R.color.green));
//        }
        holder.tvResult.setText("入手".equals(bean.type) ? "" : bean.result + "");

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        if (listener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemClick(bean);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ReshltHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_is_right)
        TextView tvIsRight;
        @BindView(R.id.tv_result)
        TextView tvResult;

        public ReshltHodler(@NonNull View view) {
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
        public void onItemClick(ResultBean bean);
    }
}

