package com.demon.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demon.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private Context context;
    private List<String> beanList;

    private int selectedIndex;

    // 1.修改对象
    public GroupAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, viewGroup, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder holder, final int i) {
        // 3.设置界面数据
        String bean = beanList.get(i);
        holder.tvGroup.setText(bean);

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        if (i == selectedIndex) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_red);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    selectedIndex = i;
                    listener.onItemClick(beanList.get(i));
                }
            }
        });
    }

    public void clickIndex(int index) {
        if (listener != null && beanList.size() > index) {
            selectedIndex = index;
            listener.onItemClick(beanList.get(index));
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class GroupHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_group)
        protected TextView tvGroup;

        public GroupHolder(@NonNull View view) {
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
