package com.demon.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demon.myapplication.Beans.MaterialBean;
import com.demon.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.AddHolder> {


    private Context context;
    private List<MaterialBean> beanList;

    // TODO 1.修改对象
    public AddAdapter(Context context, List<MaterialBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AddHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_add, viewGroup, false);
        return new AddHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddHolder holder, final int i) {
        // 3.设置界面数据
        final MaterialBean bean = beanList.get(i);
        holder.tvOrder.setText((i + 1) + "");
        holder.tvName.setText(bean.name);
        holder.tvNumber.setText(String.format("%.1f", bean.number));
        holder.tvUnit.setText(bean.unit);

        if (i % 2 == 0) {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.viewGroup.setBackgroundResource(R.drawable.shape_box_gray);
        }

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
                    listener.onItemLongClick(beanList.get(i));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AddHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_type)
        LinearLayout viewGroup;
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_unit)
        TextView tvUnit;

        public AddHolder(@NonNull View view) {
            super(view);
            // 4.查找界面控件
            ButterKnife.bind(this, view);
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MaterialBean bean);

        void onItemLongClick(MaterialBean bean);
    }
}
