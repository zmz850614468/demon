package com.demon.fit.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private Context context;
    private List<ItemBean> beanList;

    // 1.修改对象
    public ItemAdapter(Context context, List<ItemBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int i) {
        // 3.设置界面数据
        ItemBean bean = beanList.get(i);

        holder.ivIcon.setBackgroundResource(bean.imageSrc);
        holder.tvName.setText(bean.name);

//        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
//        } else {
//            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
//        }

        if (listener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(bean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ItemHolder(@NonNull View view) {
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
        public void onItemClick(ItemBean bean);
    }
}
