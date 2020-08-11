package com.lilanz.tooldemo.multiplex.BLELIB;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已匹配 的蓝牙适配器
 */
public class BleScanAdapter extends RecyclerView.Adapter<BleScanAdapter.Holder> {

    private Context context;
    private List<String> beanList;
    private int selectedIndex = -1;

    // 1.修改对象
    public BleScanAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        String address = SharePreferencesUtil.getBleAddress(context);
        for (int i = 0; i < beanList.size(); i++) {
            if (address.equals(beanList.get(i))) {
                selectedIndex = i;
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_ble, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int i) {
        // 3.设置界面数据
        holder.tvAddress.setText(beanList.get(i));
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = i;
                if (listener != null) {
                    listener.onItemClick(beanList.get(i));
                }
                notifyDataSetChanged();
            }
        });
        if (selectedIndex == i) {
            holder.layoutItem.setBackgroundResource(R.drawable.shape_box_red);
        } else {
            holder.layoutItem.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item)
        public ViewGroup layoutItem;
        @BindView(R.id.tv_name)
        public TextView tvName;         // 名称
        @BindView(R.id.tv_address)
        public TextView tvAddress;      // 地址

        public Holder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
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
