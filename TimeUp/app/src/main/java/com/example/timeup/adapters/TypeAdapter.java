package com.example.timeup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timeup.R;
import com.example.timeup.beans.TypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeHolder> {


    private Context context;
    private List<TypeBean> beanList;

    // 1.修改对象
    public TypeAdapter(Context context, List<TypeBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public TypeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_type, viewGroup, false);
        return new TypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeHolder holder, final int i) {
        // 3.设置界面数据
        final TypeBean bean = beanList.get(i);
        holder.tvOrder.setText((i + 1) + "");
        holder.tvName.setText(bean.name);
        holder.tvDuring.setText(bean.during + "");

        holder.viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(beanList.get(i));
                }
            }
        });

        holder.viewGroup.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClick(beanList.get(i));
                }
                return true;
            }
        });

        holder.tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onStartClick(beanList.get(i));
                }
            }
        });

        if (i % 2 == 0) {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_gray);
        } else {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class TypeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_type)
        LinearLayout viewGroup;
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_during)
        TextView tvDuring;
        @BindView(R.id.tv_start)
        TextView tvStart;

        public TypeHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvStart.setText("Go");
            tvStart.setTextColor(context.getResources().getColor(R.color.red));
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(TypeBean bean);

        void onStartClick(TypeBean bean);

        void onLongClick(TypeBean bean);
    }
}
