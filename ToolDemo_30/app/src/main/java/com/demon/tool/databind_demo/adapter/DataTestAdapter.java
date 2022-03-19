package com.demon.tool.databind_demo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.tool.R;
import com.demon.tool.databind_demo.bean.DataTestBean;
import com.demon.tool.databinding.ItemDataTestBinding;

import java.util.ArrayList;
import java.util.List;


public class DataTestAdapter extends RecyclerView.Adapter<DataTestAdapter.DataHolder> {

    private Context context;
    private List<DataTestBean> beanList;

    // 1.修改对象
    public DataTestAdapter(Context context, List<DataTestBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemDataTestBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_data_test, viewGroup, false);
        DataHolder holder = new DataHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
//        // 2.修改布局
//        View view = LayoutInflater.from(context).inflate(R.layout.item_data_test, viewGroup, false);
//        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, final int i) {
        // 3.设置界面数据

        holder.getBinding().setBean(beanList.get(i));
        holder.getBinding().executePendingBindings();

//        DataTestBean bean = beanList.get(i);
//
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

    public class DataHolder extends RecyclerView.ViewHolder {

        private ItemDataTestBinding binding;

        public DataHolder(@NonNull View view) {
            super(view);
            // 4.查找界面控件
        }

        public void setBinding(ItemDataTestBinding binding) {
            this.binding = binding;
        }

        public ItemDataTestBinding getBinding() {
            return binding;
        }
    }

}
