package com.demon.accountmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.bean.AccountBean;
import com.demon.accountmanagement.databinding.ItemAccountBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    private Context context;
    private List<AccountBean> beanList;

    private boolean isShowPwd;

    // 1.修改对象
    public AccountAdapter(Context context, List<AccountBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public AccountHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemAccountBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_account, viewGroup, false);
        AccountHolder holder = new AccountHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountHolder holder, final int i) {
        // 3.设置界面数据

        holder.getBinding().setBean(beanList.get(i));
        holder.getBinding().setOrder((i + 1) + "");
        holder.getBinding().setIsShowPwd(isShowPwd);
        holder.getBinding().executePendingBindings();
        
        holder.itemView.setOnLongClickListener(view -> {
            if (onLongClicked != null) {
                onLongClicked.onLongClicked(beanList.get(i));
            }
            return true;
        });
    }

    public void setPwdShow(boolean b) {
        this.isShowPwd = b;
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class AccountHolder extends RecyclerView.ViewHolder {

        private ItemAccountBinding binding;

        public AccountHolder(@NonNull View view) {
            super(view);
            // 4.查找界面控件
        }

        public void setBinding(ItemAccountBinding binding) {
            this.binding = binding;
        }

        public ItemAccountBinding getBinding() {
            return binding;
        }
    }

    private OnLongClicked onLongClicked;

    public void setOnLongClicked(OnLongClicked onLongClicked) {
        this.onLongClicked = onLongClicked;
    }

    public interface OnLongClicked {
        void onLongClicked(AccountBean baan);
    }

}