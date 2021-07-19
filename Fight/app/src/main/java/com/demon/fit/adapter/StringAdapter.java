package com.demon.fit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StringAdapter extends RecyclerView.Adapter<StringAdapter.StringHolder> {

    private Context context;
    private List<String> beanList;

    // 1.修改对象
    public StringAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public StringHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_string, viewGroup, false);
        return new StringHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StringHolder holder, final int i) {
        // 3.设置界面数据
        String bean = beanList.get(i);

        holder.tvOrder.setText((i+1) + "");
        holder.tvContent.setText(bean);

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class StringHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public StringHolder(@NonNull View view) {
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
        public void onItemClick(String bean);
    }
}

