package com.demon.accountmanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.databinding.ItemAccountGroupBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountGroupAdapter extends RecyclerView.Adapter<AccountGroupAdapter.AccountGroupHolder> {

    private Context context;
    private List<String> beanList;

    // 1.修改对象
    public AccountGroupAdapter(Context context, List<String> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AccountGroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemAccountGroupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_account_group, viewGroup, false);
        AccountGroupHolder holder = new AccountGroupHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountGroupHolder holder, final int i) {
        // 3.设置界面数据

        holder.getBinding().setBean(beanList.get(i));
        holder.getBinding().executePendingBindings();

        if (onClickListener != null) {
            holder.getBinding().getRoot().setOnClickListener(v -> {
                onClickListener.onClicked(beanList.get(i));
            });

            holder.getBinding().getRoot().setOnLongClickListener(v -> {
                onClickListener.onLongClicked(beanList.get(i));
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AccountGroupHolder extends RecyclerView.ViewHolder {

        private ItemAccountGroupBinding binding;

        public AccountGroupHolder(@NonNull View view) {
            super(view);
            // 4.查找界面控件
        }

        public void setBinding(ItemAccountGroupBinding binding) {
            this.binding = binding;
        }

        public ItemAccountGroupBinding getBinding() {
            return binding;
        }
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClicked(String name);

        void onLongClicked(String name);
    }

    private void showLog(String msg) {
        Log.e("AccountGroupAdapter", msg);
    }
}
