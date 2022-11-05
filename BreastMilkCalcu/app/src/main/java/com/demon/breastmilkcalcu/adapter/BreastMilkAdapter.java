package com.demon.breastmilkcalcu.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.breastmilkcalcu.R;
import com.demon.breastmilkcalcu.bean.BreastMilkBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BreastMilkAdapter extends RecyclerView.Adapter<BreastMilkAdapter.BreastMilkHolder> {

    private Context context;
    private List<BreastMilkBean> beanList;

    // 1.修改对象
    public BreastMilkAdapter(Context context, List<BreastMilkBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public BreastMilkHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_breast_milk, viewGroup, false);
        return new BreastMilkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BreastMilkHolder holder, final int i) {
        // 3.设置界面数据
        BreastMilkBean bean = beanList.get(i);

        holder.tvOrder.setText((i + 1) + "");
        holder.tvToadyData.setText(bean.todayData);
        holder.tvToadyAppend.setText(bean.todayAppend);

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

    public class BreastMilkHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        public TextView tvOrder;
        @BindView(R.id.tv_today_data)
        public TextView tvToadyData;
        @BindView(R.id.tv_today_append)
        public TextView tvToadyAppend;

        public BreastMilkHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvToadyData.setGravity(Gravity.CENTER_VERTICAL);
            tvToadyAppend.setGravity(Gravity.CENTER_VERTICAL);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(BreastMilkBean bean);
    }
}
