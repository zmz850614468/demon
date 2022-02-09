package com.demon.fit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.demon.fit.R;
import com.demon.fit.bean.CostBean;
import com.demon.fit.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CostDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.et_product)
    EditText etProduct;
    @BindView(R.id.et_cost)
    EditText etCost;

    private Context context;

    public CostDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cost);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void updateUI(CostBean bean) {
        etProduct.setText(bean.name);
        etCost.setText(bean.cost + "");
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        if (listener != null) {
            String product = etProduct.getText().toString();
            String cost = etCost.getText().toString();
            if (StringUtil.isEmpty(product) || StringUtil.isEmpty(cost)) {
                showToast("数据不能为空");
                return;
            }

            listener.onConfirm(product, Integer.parseInt(cost));
        }
        etProduct.setText("");
        etCost.setText("");
        dismiss();
    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onConfirm(String product, int cost);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
