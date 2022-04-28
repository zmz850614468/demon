package com.demon.accountmanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.demon.accountmanagement.R;
import com.demon.accountmanagement.databinding.DialogInputBinding;

public class InputDialog extends Dialog implements DialogInterface.OnClickListener {

    public ObservableField<String> inputEdit = new ObservableField<>();

    private DialogInputBinding binding;
    private Context context;

    public InputDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        binding = DataBindingUtil.bind(findViewById(R.id.layout_type));
        binding.setDialog(this);
        setCanceledOnTouchOutside(true);
    }

    public void updateHin(String hint) {
        binding.etInput.setHint(hint);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void onOkClicked(View v) {
        if (listener != null) {
            listener.onConfirm(inputEdit.get());
        }
        inputEdit.set("");
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
