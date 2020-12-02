package com.lilanz.wificonnect.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.controls.AppDataControl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.SongHolder> {

    private Context context;
    private List<SongBean> beanList;

    // 1.修改对象
    public FileAdapter(Context context, List<SongBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }


    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_file, viewGroup, false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder holder, final int i) {
        // 3.设置界面数据
        SongBean bean = beanList.get(i);
        holder.tvOrder.setText(i + 1 + "");
        int index = bean.name.lastIndexOf(".");
        holder.tvName.setText(bean.name.substring(0, index));

        if (bean.isServiceExit) {
            holder.tvExit.setText("是");
            holder.cbSelect.setVisibility(View.INVISIBLE);
        } else {
            holder.tvExit.setText("否");
            holder.cbSelect.setVisibility(View.VISIBLE);
        }

        holder.cbSelect.setChecked(bean.isSelect);

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }

        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.isSelect = isChecked;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cbSelect.setChecked(!bean.isSelect);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_order)
        TextView tvOrder;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_exit)
        TextView tvExit;
        @BindView(R.id.tv_select)
        TextView tvSelect;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        public SongHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
            tvOrder.setTextSize(15);
            tvName.setTextSize(15);
            tvExit.setTextSize(15);
            tvSelect.setVisibility(View.GONE);
            cbSelect.setVisibility(View.VISIBLE);
            // 4.查找界面控件
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(SongBean bean);
    }
}
