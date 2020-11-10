package com.demon.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.demon.myapplication.R;

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


    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        if (listener != null) {
            String str = etInput.getText().toString();
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
}
