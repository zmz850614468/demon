package com.lilanz.tooldemo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListDialog extends Dialog implements DialogInterface.OnClickListener {

    private Context context;
    private RecyclerView recyclerView;
    private AccountAdapter listAdapter;

    private Button btOk;

    private List<String> beanList;
    private String selectedBean;

    public ListDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ble_device);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);

        initUI();
    }

    public void update(List<String> list) {
        selectedBean = null;
        listAdapter.update(null);
        beanList.clear();
        beanList.addAll(list);
        Collections.sort(beanList);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        beanList = new ArrayList<>();
        listAdapter = new AccountAdapter(context, beanList);
        recyclerView = findViewById(R.id.recycle_account);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(listAdapter);
        listAdapter.setListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String bean) {
                selectedBean = bean;
            }
        });
    }

    @OnClick(R.id.bt_ok)
    public void onClicked(View v) {
        if (StringUtil.isEmpty(selectedBean)) {
            showToast("请先选择新的分组！");
            return;
        }

        if (listener != null) {
            listener.onConfirm(selectedBean);
        }
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
