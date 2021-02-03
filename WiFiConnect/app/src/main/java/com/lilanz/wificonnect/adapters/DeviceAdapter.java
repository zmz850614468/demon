package com.lilanz.wificonnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activitys.AddDeviceActivity;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.dialogs.FanControlDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private Context context;
    private List<DeviceBean> beanList;

    private FanControlDialog fanControlDialog;

    // 1.修改对象
    public DeviceAdapter(Context context, List<DeviceBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        initUI();
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, viewGroup, false);
        return new DeviceHolder(view);
    }

    private long lastClick;

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, final int i) {
        // 3.设置界面数据
        DeviceBean bean = beanList.get(i);
        holder.tvName.setText(bean.name);
        holder.tvPosition.setText(bean.devicePosition);

        if ("开关式".equals(bean.controlType)) {
            holder.ivStatus.setVisibility(View.VISIBLE);
            if ("open".equals(bean.status)) {
                holder.ivStatus.setBackgroundResource(R.drawable.shape_circle_green);
            } else {
                holder.ivStatus.setBackgroundResource(R.drawable.shape_circle_black);
            }
        } else {
            holder.ivStatus.setVisibility(View.GONE);
        }

        switch (bean.deviceType) {
            case "灯":
                holder.ivPic.setBackgroundResource(R.mipmap.lamp);
                break;
            case "风扇":
                holder.ivPic.setBackgroundResource(R.mipmap.electric_fans);
                break;
            case "热水器":
                holder.ivPic.setBackgroundResource(R.mipmap.water_heater);
                break;
            case "电饭锅":
                holder.ivPic.setBackgroundResource(R.mipmap.electric_pot);
                break;
        }

        holder.ivControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fanControlDialog.update(beanList.get(i));
                fanControlDialog.show();
            }
        });

        if (listener != null) {
            holder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(beanList.get(i), "open\n\r");
                }
            });
            holder.btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(beanList.get(i), "close\n\r");
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currTime = System.currentTimeMillis();
                if (currTime - lastClick < 1000) {
                    Intent intent = new Intent(context, AddDeviceActivity.class);
                    intent.putExtra("device", beanList.get(i).toString());
                    context.startActivity(intent);
                }
                lastClick = currTime;
            }
        });
    }

    private void initUI() {
        fanControlDialog = new FanControlDialog(context, R.style.DialogStyleOne);
        fanControlDialog.show();
        fanControlDialog.dismiss();
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
        @BindView(R.id.bt_open)
        TextView btOpen;
        @BindView(R.id.bt_close)
        TextView btClose;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.tv_position)
        TextView tvPosition;
        @BindView(R.id.iv_control)
        ImageView ivControl;    // 设备控制界面

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
        public void onItemClick(DeviceBean bean, String operate);
    }
}

