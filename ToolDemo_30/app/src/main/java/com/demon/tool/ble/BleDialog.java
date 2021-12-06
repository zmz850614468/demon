package com.demon.tool.ble;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.tool.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.rv_ble_device)
    protected RecyclerView recyclerView;
    private BleAdapter bleDeviceAdapter;

    private Context context;
    private List<String> bleDeviceList;
    private String selectedDevice;

    public BleDialog(Context context, int inputType) {
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

    @OnClick(R.id.bt_ok)
    public void onOkClicked(View v) {
        if (onBleSelectedListener != null) {
            onBleSelectedListener.onCallBack(selectedDevice);
        }
        dismiss();
    }

    public void updateData(List<String> bleNameList, String selectedName) {
        this.selectedDevice = selectedName;
        bleDeviceList.clear();
        bleDeviceList.addAll(bleNameList);
        bleDeviceAdapter.selectedName(selectedName);
        bleDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        bleDeviceList = new ArrayList<>();
        bleDeviceAdapter = new BleAdapter(context, bleDeviceList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bleDeviceAdapter);
        bleDeviceAdapter.setListener(bean -> selectedDevice = bean);
    }

    //  ====================== 时间监听 ===========================

    private OnBleSelectedListener onBleSelectedListener;

    public void setOnBleSelectedListener(OnBleSelectedListener onBleSelectedListener) {
        this.onBleSelectedListener = onBleSelectedListener;
    }

    public interface OnBleSelectedListener {
        void onCallBack(String msg);
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
