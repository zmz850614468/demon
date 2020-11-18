package com.lilanz.tooldemo.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lilanz.tooldemo.R;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    private Context context;
    private List<String> beanList;
    private int selectedInde;

    public AccountAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        selectedInde = -1;
    }

    public void update(String bean) {
        selectedInde = -1;
        if (bean != null) {
            for (int i = 0; i < beanList.size(); i++) {
                if (beanList.get(i).equals(bean)) {
                    selectedInde = i;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, viewGroup, false);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, final int i) {
        final String bean = beanList.get(i);
        holder.tvKey.setText(bean);
        holder.layoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedInde = i;
                if (listener != null) {
                    listener.onItemClick(bean);
                }
                notifyDataSetChanged();
            }
        });

        if (i == selectedInde) {
            holder.tvKey.setBackgroundResource(R.drawable.shape_box_red);
        } else {
            holder.tvKey.setBackgroundResource(R.drawable.shape_box_white);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {

        private ViewGroup layoutAccount;
        public TextView tvKey;

        public AccountHolder(@NonNull View itemView) {
            super(itemView);
            layoutAccount = itemView.findViewById(R.id.item_ble_device);
            tvKey = itemView.findViewById(R.id.tv_key);
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
