package com.demon.cameratest.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.cameratest.R;

import java.util.ArrayList;
import java.util.List;

public class StrAdapter extends RecyclerView.Adapter<StrAdapter.StrHolder> {

    private Context context;
    private List<String> beanList;

    // 1.修改对象
    public StrAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    public void add(String data) {
        beanList.add(0, data);
    }

    @NonNull
    @Override
    public StrHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), i, viewGroup, false);
        return new StrHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_str;
    }

    @Override
    public void onBindViewHolder(@NonNull StrHolder holder, final int i) {
        // 3.设置界面数据
        String bean = beanList.get(i);

        holder.binding.setVariable(BR.msg, bean);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class StrHolder extends RecyclerView.ViewHolder {
        ViewDataBinding binding;

        public StrHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
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
