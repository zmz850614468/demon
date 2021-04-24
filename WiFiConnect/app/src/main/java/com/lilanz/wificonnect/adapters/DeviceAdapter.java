package com.lilanz.wificonnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.AddDeviceActivity;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.data.myenum.DeviceType;
import com.lilanz.wificonnect.dialogs.AirConditionDialog;
import com.lilanz.wificonnect.dialogs.FanControlDialog;
import com.lilanz.wificonnect.dialogs.PotControlDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    private Context context;
    private List<DeviceBean> beanList;

    private FanControlDialog fanControlDialog;
    private AirConditionDialog airConditionDialog;
    private PotControlDialog potControlDialog;

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

        holder.ivPic.setBackgroundResource(DeviceType.getImgResoure(bean.deviceType));
        if (bean.deviceType == DeviceType.ELECTRIC_FAN || bean.deviceType == DeviceType.AIR_CONDITION
                || bean.deviceType == DeviceType.ELECTRIC_POT) {
            holder.ivControl.setVisibility(View.VISIBLE);
        } else {
            holder.ivControl.setVisibility(View.GONE);
        }

        holder.ivControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beanList.get(i).deviceType == DeviceType.ELECTRIC_FAN) {
                    fanControlDialog.update(beanList.get(i));
                    fanControlDialog.show();
                } else if (beanList.get(i).deviceType == DeviceType.AIR_CONDITION) {
                    airConditionDialog.update(beanList.get(i));
                    airConditionDialog.show();
                } else if (beanList.get(i).deviceType == DeviceType.ELECTRIC_POT) {
                    potControlDialog.update(beanList.get(i));
                    potControlDialog.show();
                }
            }
        });

        if (listener != null) {
            holder.btOpen.setOnClickListener(new View.OnClickListener() {
                long lastClickTime;

                @Override
                public void onClick(View v) {
                    if (beanList.get(i).deviceType == DeviceType.ELECTRIC_POT) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime < 2000) {
                            showToast("请不要连续点击，稍后再尝试!");
                            return;
                        }
                        lastClickTime = currentTime;

                        potControlDialog.update(beanList.get(i));
                        potControlDialog.onClicked(R.id.tv_function_1);
                    } else {
                        listener.onItemClick(beanList.get(i), beanList.get(i).getControlData(DeviceBean.STATUS_OPEN, null));
                    }
                }
            });
            holder.btClose.setOnClickListener(new View.OnClickListener() {
                long lastClickTime;

                @Override
                public void onClick(View v) {
                    if (beanList.get(i).deviceType == DeviceType.ELECTRIC_POT) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime < 2000) {
                            showToast("请不要连续点击，稍后再尝试!");
                            return;
                        }
                        lastClickTime = currentTime;

                        potControlDialog.update(beanList.get(i));
                        potControlDialog.onClicked(R.id.tv_function_4);
                    } else {
                        listener.onItemClick(beanList.get(i), beanList.get(i).getControlData(DeviceBean.STATUS_CLOSE, null));
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currTime = System.currentTimeMillis();
                if (currTime - lastClick < 1000) {
                    Intent intent = new Intent(context, AddDeviceActivity.class);
                    intent.putExtra("action", AddDeviceActivity.ACTION_UPDATE);
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
        Window window = fanControlDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);

        airConditionDialog = new AirConditionDialog(context, R.style.DialogStyleOne);
        airConditionDialog.show();
        airConditionDialog.dismiss();
        window = airConditionDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);

        potControlDialog = new PotControlDialog(context, R.style.DialogStyleOne);
        potControlDialog.show();
        potControlDialog.dismiss();
        window = potControlDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.color.transparent);
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

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

