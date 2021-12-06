package com.demon.remotecontrol.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.DeviceMemoBean;
import com.demon.remotecontrol.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.AccountHolder> {

    private Context context;
    private List<DeviceMemoBean> beanList;
    private int selectedInde;

    public DeviceAdapter(Context context, List<DeviceMemoBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        selectedInde = -1;
    }

    public void selectedName(String bean) {
        selectedInde = -1;
        if (bean != null) {
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).equals(bean)) {
                    selectedInde = i;
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, viewGroup, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, final int i) {
        final DeviceMemoBean bean = beanList.get(i);
        holder.tvKey.setText(bean.deviceId);

        if (StringUtil.isEmpty(bean.deviceMemo)) {
            holder.tvMemo.setText("未备注");
        } else {
            holder.tvMemo.setText(bean.deviceMemo);
        }

        if (listener != null) {
            holder.layoutAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedInde = i;
                    listener.onItemClick(bean);
                    notifyDataSetChanged();
                }
            });

            holder.layoutAccount.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(bean);
                    return true;
                }
            });
        }

        if (i == selectedInde) {
            holder.layoutAccount.setBackgroundResource(R.drawable.shape_box_red);
        } else {
            holder.layoutAccount.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {

        private ViewGroup layoutAccount;
        public TextView tvKey;
        TextView tvMemo;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            layoutAccount = itemView.findViewById(R.id.item_ble_device);
            tvKey = itemView.findViewById(R.id.tv_key);
            tvMemo = itemView.findViewById(R.id.tv_memo);
        }
    }

    public void setSelectedInde(int selectedInde) {
        this.selectedInde = selectedInde;
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(DeviceMemoBean bean);

        void onLongClick(DeviceMemoBean bean);
    }
}
