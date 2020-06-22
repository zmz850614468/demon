package com.lilanz.tooldemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.beans.BaseBean;

import java.util.ArrayList;
import java.util.List;

public class PianShuAdapter extends RecyclerView.Adapter<PianShuAdapter.PianShuHolder> {

    private Context context;
    private List<BaseBean> beanList;

    // TODO 1.修改对象
    public PianShuAdapter(Context context, List<BaseBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public PianShuHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // TODO 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_base_adapter, viewGroup, false);
        return new PianShuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PianShuHolder pianShuHolder, final int i) {
        // TODO 3.设置界面数据
        BaseBean bean = beanList.get(i);
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class PianShuHolder extends RecyclerView.ViewHolder {

        public PianShuHolder(@NonNull View itemView) {
            super(itemView);
            // TODO 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(BaseBean bean);
    }
}
