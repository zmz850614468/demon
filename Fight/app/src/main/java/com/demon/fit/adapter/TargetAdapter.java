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

public class TargetAdapter extends RecyclerView.Adapter<TargetAdapter.TargetHolder> {

    private Context context;
    private List<String> beanList;

    // 1.修改对象
    public TargetAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public TargetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_target, viewGroup, false);
        return new TargetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetHolder holder, final int i) {
        // 3.设置界面数据
        String bean = beanList.get(i);
        holder.tvTarget.setText(bean);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class TargetHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_target)
        TextView tvTarget;

        public TargetHolder(@NonNull View view) {
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
