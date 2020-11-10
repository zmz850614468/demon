package com.example.timeup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timeup.R;
import com.example.timeup.beans.TypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExecuteAdapter extends RecyclerView.Adapter<ExecuteAdapter.TypeHolder> {


    private Context context;
    private List<TypeBean> beanList;

    // 1.修改对象
    public ExecuteAdapter(Context context, List<TypeBean> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_execute, viewGroup, false);
        return new TypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeHolder holder, final int i) {
        // 3.设置界面数据
        final TypeBean bean = beanList.get(i);
        holder.tvOrder.setText((i + 1) + "");
        holder.tvName.setText(bean.name);
        holder.tvDuring.setText(bean.getSecond());

        holder.viewGroup.setOnClickListener(new View.OnClickListener() {
            private long lastClickTime;

            @Override
            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onItemClick(beanList.get(i));
//                }

                long currentTime = System.currentTimeMillis();

                if (listener != null) {
                    if (beanList.get(i).during > 0 && currentTime - lastClickTime < 1000) {
                        listener.onDeleteClick(beanList.get(i));
                    } else {
                        listener.onItemClick(beanList.get(i));
                    }
                }

                lastClickTime = currentTime;

            }
        });

//        holder.viewGroup.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (listener != null) {
//                    listener.onLongClick(beanList.get(i));
//                }
//                return true;
//            }
//        });

        if (i % 2 == 0) {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_gray);
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

        public TypeHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvOrder.setTextSize(16);
            tvName.setTextSize(16);
            tvDuring.setTextSize(16);
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

        void onDeleteClick(TypeBean bean);
    }
}
