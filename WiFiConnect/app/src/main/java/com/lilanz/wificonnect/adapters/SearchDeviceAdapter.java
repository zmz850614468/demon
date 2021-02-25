package com.lilanz.wificonnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.data.myenum.DeviceType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchDeviceAdapter extends RecyclerView.Adapter<SearchDeviceAdapter.DeviceHolder> {

    private Context context;
    private List<DeviceBean> beanList;

    // 1.修改对象
    public SearchDeviceAdapter(Context context, List<DeviceBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_serarch_device, viewGroup, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, final int i) {
        // 3.设置界面数据
        DeviceBean bean = beanList.get(i);

        holder.tvName.setText(bean.deviceType.name);
        holder.ivPic.setBackgroundResource(DeviceType.getImgResoure(bean.deviceType));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(beanList.get(i));
                }
            }
        });

        if (i % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView ivPic;
        @BindView(R.id.tv_name)
        TextView tvName;

        public DeviceHolder(@NonNull View view) {
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
        public void onItemClick(DeviceBean bean);
    }
}

