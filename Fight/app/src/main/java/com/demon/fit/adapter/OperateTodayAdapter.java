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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.util.StringUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OperateTodayAdapter extends RecyclerView.Adapter<OperateTodayAdapter.OperateTodayHolder> {

    private Context context;
    private List<OperateTodayBean> beanList;

    // 1.修改对象
    public OperateTodayAdapter(Context context, List<OperateTodayBean> list) {
        this.context = context;
        this.beanList = list;
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        if (beanList.isEmpty()) {
            beanList.add(new OperateTodayBean());
        }
    }

    @NonNull
    @Override
    public OperateTodayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // 2.修改布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_operate_today, viewGroup, false);
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
        holder.etInPrice.setText(bean.inPrice + "");
        holder.etOutPrice.setText(bean.outPrice + "");

        // 最后一个
//        if (beanList.size() == i + 1) {
//            holder.etName.setOnFocusChangeListener((v, hasFocus) -> {
//                if (!hasFocus && !StringUtil.isEmpty(bean.name)) {
//                    beanList.add(new OperateTodayBean());
//                    showLog(new Gson().toJson(beanList));
//                    notifyDataSetChanged();
//                }
//            });
//        }


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

    public class OperateTodayHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_in_type)
        TextView tvInType;
        @BindView(R.id.et_name)
        EditText etName;
        @BindView(R.id.et_in_price)
        EditText etInPrice;
        @BindView(R.id.et_out_price)
        EditText etOutPrice;

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
}

