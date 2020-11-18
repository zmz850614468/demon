package com.demon.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demon.myapplication.Beans.GroupBean;
import com.demon.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeiFangAdapter extends RecyclerView.Adapter<PeiFangAdapter.PeiFangHolder> {

    private Context context;
    private List<GroupBean> beanList;

    // 1.修改对象
    public PeiFangAdapter(Context context, List<GroupBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public PeiFangHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_pei_fang, viewGroup, false);
        return new PeiFangHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeiFangHolder holder, final int i) {
        // 3.设置界面数据
        GroupBean bean = beanList.get(i);
        holder.tvPeiFang.setText(bean.peiFangName);

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(beanList.get(i));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onDeleteClick(beanList.get(i));
                }
                return true;
            }
        });

        holder.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onChangeClick(beanList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class PeiFangHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_pei_fang)
        protected TextView tvPeiFang;
        @BindView(R.id.tv_change)
        protected TextView tvChange;

        public PeiFangHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            // 4.查找界面控件

            tvChange.setText("换组");
            tvChange.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(GroupBean bean);

        void onChangeClick(GroupBean bean);

        void onDeleteClick(GroupBean bean);
    }
}
