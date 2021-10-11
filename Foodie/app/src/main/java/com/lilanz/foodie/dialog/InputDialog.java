package com.lilanz.foodie.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lilanz.foodie.R;
import com.lilanz.foodie.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class InputDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.et_input)
    EditText etInput;

    private Context context;

    public InputDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void setInputHint(String hint) {
        etInput.setHint(hint);
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        String str = etInput.getText().toString();
        if (StringUtil.isEmpty(str)) {
            showToast("内容不能为空");
            return;
        }
        if (listener != null) {
            listener.onConfirm(str);
        }
        etInput.setText("");
        dismiss();
    }

    //  ====================== 时间监听 ===========================

    private OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {

        void onConfirm(String str);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
