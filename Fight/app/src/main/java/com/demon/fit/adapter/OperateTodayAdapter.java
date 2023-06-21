package com.demon.fit.adapter;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.dialog.SelectPriceDialog;
import com.demon.fit.util.SharePreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateTodayAdapter extends RecyclerView.Adapter<OperateTodayAdapter.OperateTodayHolder> {

    private Context context;
    private List<OperateTodayBean> beanList;
    private SelectPriceDialog selectPriceDialog;

    private TextView selectedTextView;
    private OperateTodayBean selectedBean;

    // 1.修改对象
    public OperateTodayAdapter(Context context, List<OperateTodayBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        if (beanList.isEmpty()) {
            beanList.add(new OperateTodayBean(context));
        }
    }

    @NonNull
    @Override
    public OperateTodayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_today, viewGroup, false);
        initDialog();
        return new OperateTodayHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OperateTodayHolder holder, final int i) {
        // 3.设置界面数据
        OperateTodayBean bean = beanList.get(i);

        holder.tvInType.setOnClickListener(v -> {
            if ("买入".equals(bean.inType)) {
                bean.inType = "卖入";
            } else {
                bean.inType = "买入";
            }
            holder.tvInType.setText(bean.inType);
        });
        if (bean.isFollow) {
            holder.tvInType.setTextColor(context.getResources().getColor(R.color.gray));
        } else {
            holder.tvInType.setTextColor(context.getResources().getColor(R.color.black));
        }
        holder.tvInType.setOnLongClickListener(v -> {
            bean.isFollow = !bean.isFollow;
            if (bean.isFollow) {
                showToast("设置为跟随单");
                holder.tvInType.setTextColor(context.getResources().getColor(R.color.gray));
            } else {
                showToast("设置为主单");
                holder.tvInType.setTextColor(context.getResources().getColor(R.color.black));
            }
            return true;
        });

        holder.etName.removeTextChangedListener(holder.nameWatchListener);
        holder.etInPrice.removeTextChangedListener(holder.inPriceListener);
        holder.etOutPrice.removeTextChangedListener(holder.outPriceListener);
        holder.etName.addTextChangedListener(holder.nameWatchListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                bean.name = s.toString();
            }
        });
        holder.tvPrice.setOnClickListener(v -> {
            selectedBean = bean;
            selectedTextView = holder.tvPrice;
            selectPriceDialog.show();

//            bean.price -= 10;
//            if (bean.price <= 0) {
//                bean.price = OperateTodayBean.MAX_PRICE;
//            }
//            SharePreferencesUtil.saveSelectedCount(context, bean.price);
//
//            holder.tvPrice.setText(bean.price + "");
        });
        holder.etInPrice.addTextChangedListener(holder.inPriceListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    bean.inPrice = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    bean.inPrice = 0;
                }
            }
        });
        holder.etOutPrice.addTextChangedListener(holder.outPriceListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    bean.outPrice = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    bean.outPrice = 0;
                }
            }
        });

        holder.tvInType.setText(bean.inType);
        holder.etName.setText(bean.name);
        holder.tvPrice.setText(bean.price + "");
        holder.etInPrice.setText(bean.inPrice + "");
        holder.etOutPrice.setText(bean.outPrice + "");
        holder.tvResult.setText(bean.result == 0 || bean.outPrice == 0 ? "" : bean.result + "");
        if (bean.result > 0) {
            holder.tvResult.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.tvResult.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (bean.isBadOperate) {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_green);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
        }
        holder.tvPrice.setOnLongClickListener(v -> {
            bean.isBadOperate = !bean.isBadOperate;
            if (bean.isBadOperate) {
                showToast("设置为糟糕操作");
                holder.itemView.setBackgroundResource(R.drawable.shape_box_green);
            } else {
                showToast("设置为常规操作");
                holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
            }
            return true;
        });

//        if (i % 2 == 0) {
//            holder.itemView.setBackgroundResource(R.drawable.shape_box_white);
//        } else {
//            holder.itemView.setBackgroundResource(R.drawable.shape_box_gray);
//        }
    }

    private void initDialog() {
        selectPriceDialog = new SelectPriceDialog(context, R.style.DialogStyleOne);
        selectPriceDialog.setListener(price -> {
            if (selectedTextView != null) {
                selectedTextView.setText("" + price);
            }
            if (selectedBean != null) {
                selectedBean.price = price;
            }
            SharePreferencesUtil.saveSelectedCount(context, price);
        });
        selectPriceDialog.show();
        selectPriceDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public class OperateTodayHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_in_type)
        TextView tvInType;
        @BindView(R.id.et_name)
        EditText etName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.et_in_price)
        EditText etInPrice;
        @BindView(R.id.et_out_price)
        EditText etOutPrice;
        @BindView(R.id.tv_result)
        TextView tvResult;

        TextWatcher nameWatchListener;
        TextWatcher inPriceListener;
        TextWatcher outPriceListener;

        public OperateTodayHolder(@NonNull View view) {
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
        public void onItemClick(OperateTodayBean bean);
    }

    private void showLog(String msg) {
        Log.e("OperateTodayAdapter", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

