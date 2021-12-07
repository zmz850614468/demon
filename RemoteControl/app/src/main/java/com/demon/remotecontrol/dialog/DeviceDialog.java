package com.demon.remotecontrol.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.bean.DeviceMemoBean;
import com.demon.remotecontrol.daos.DBControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDialog extends Dialog implements DialogInterface.OnClickListener {

    @BindView(R.id.rv_ble_device)
    protected RecyclerView recyclerView;
    private DeviceAdapter bleDeviceAdapter;

    private Context context;
    private List<DeviceMemoBean> bleDeviceList;
    private String selectedDevice;

    private InputDialog inputDialog;

    private List<DeviceMemoBean> dbDeviceMemoList;

    public DeviceDialog(Context context, int inputType) {
        super(context, inputType);
        this.context = context;

        initDialog();
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
        dbDeviceMemoList = DBControl.queryAll(context, DeviceMemoBean.class);

        bleDeviceList.clear();
        for (DeviceMemoBean bean : dbDeviceMemoList) {
            if (bleNameList.contains(bean.deviceId)) {
                bleDeviceList.add(bean);
                bleNameList.remove(bean.deviceId);
            }
        }
        for (String s : bleNameList) {
            DeviceMemoBean bean = new DeviceMemoBean();
            bean.deviceId = s;
            bleDeviceList.add(bean);
        }

        bleDeviceAdapter.selectedName(selectedDevice);
        bleDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    private void initUI() {
        bleDeviceList = new ArrayList<>();
        bleDeviceAdapter = new DeviceAdapter(context, bleDeviceList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bleDeviceAdapter);
        bleDeviceAdapter.setListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceMemoBean bean) {
                selectedDevice = bean.deviceId;
            }

            @Override
            public void onLongClick(DeviceMemoBean bean) {
                inputDialog.update(bean);
                inputDialog.show();
            }
        });
    }

    private void initDialog() {
        inputDialog = new InputDialog(context, R.style.DialogStyleOne);
        inputDialog.setListener(bean -> {
            DBControl.createOrUpdate(context, DeviceMemoBean.class, bean);
            if (bean.id == 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("device_id", bean.deviceId);
                List<DeviceMemoBean> list = DBControl.queryByColumn(context, DeviceMemoBean.class, map);
                if (!list.isEmpty()) {
                    bean = list.get(0);
                    dbDeviceMemoList.add(list.get(0));
                }
            }

            for (DeviceMemoBean deviceMemoBean : bleDeviceList) {
                if (deviceMemoBean.deviceId.equals(bean.deviceId)) {
                    deviceMemoBean.deviceMemo = bean.deviceMemo;
                    break;
                }
            }
            bleDeviceAdapter.notifyDataSetChanged();
        });
        inputDialog.show();
        inputDialog.dismiss();
    }

    //  ====================== 时间监听 ===========================

    private OnDeviceSelectedListener onBleSelectedListener;

    public void setOnDeviceSelectedListener(OnDeviceSelectedListener onBleSelectedListener) {
        this.onBleSelectedListener = onBleSelectedListener;
    }

    public interface OnDeviceSelectedListener {
        void onCallBack(String msg);
    }


    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
