package com.demon.fit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.fit.R;
import com.demon.fit.adapter.SelectPriceAdapter;
import com.demon.fit.bean.CostBean;
import com.demon.fit.util.SharePreferencesUtil;
import com.demon.fit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectPriceDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.rv_price)
    protected RecyclerView priceRecycler;
    private SelectPriceAdapter priceAdapter;

    private List<String> priceList;

    private Context context;

    public SelectPriceDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_price);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        initAdapter();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        if (listener != null) {
            String price = etPrice.getText().toString();
            if (StringUtil.isEmpty(price)) {
                showToast("内容不能为空");
                return;
            }
            if (priceList.contains(price)) {
                priceList.remove(price);
            }
            priceList.add(0, price);
            if (priceList.size() > 16) {
                priceList.remove(priceList.size() - 1);
            }
            listener.onConfirm(Integer.parseInt(price));
            priceAdapter.notifyDataSetChanged();
            SharePreferencesUtil.savePriceList(context, priceList);
            etPrice.setText("");
            dismiss();
        }
    }


    private void initAdapter() {
        priceList = SharePreferencesUtil.getPriceList(context);

        priceAdapter = new SelectPriceAdapter(context, priceList);
//        LinearLayoutManager manager = new LinearLayoutManager(context);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        priceRecycler.setLayoutManager(manager);
        priceRecycler.setAdapter(priceAdapter);

        priceAdapter.setListener(bean -> {
            priceList.remove(bean);
            priceList.add(0, bean);
            priceAdapter.notifyDataSetChanged();
            SharePreferencesUtil.savePriceList(context, priceList);
            listener.onConfirm(Integer.parseInt(bean));
            dismiss();
        });
    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onConfirm(int price);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
