package com.lilanz.wificonnect.activity_new;

import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activitys.AddDeviceActivity;
import com.lilanz.wificonnect.activitys.App;
import com.lilanz.wificonnect.adapters.DeviceAdapter;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceListActivity extends Activity {

    public static boolean needUpdate = false;

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    @BindView(R.id.rv_device)
    protected RecyclerView deviceRecycler;
    private DeviceAdapter deviceAdapter;
    private List<DeviceBean> deviceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);

        initData();
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needUpdate) {
            onRefreshClicked(null);
            needUpdate = false;
        }
    }

    @OnClick(R.id.tv_add_device)
    public void onAddDeviceClicked(View v) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked(View v) {
        List<DeviceBean> list = DBControl.quaryAll(this, DeviceBean.class);
        deviceList.clear();
        deviceList.addAll(list);
        deviceAdapter.notifyDataSetChanged();
        showToast("数据更新成功!");

        for (DeviceBean deviceBean : deviceList) {
            Esp8266ControlBean controlBean = new Esp8266ControlBean();
            controlBean.ip = deviceBean.ip;
            controlBean.port = deviceBean.port;
            controlBean.control = "query\n\r";
            DeviceOkSocketControl.getInstance(this).sendControlMsg(controlBean);
        }
    }

    private void initAdapter() {
        deviceAdapter = new DeviceAdapter(this, deviceList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        deviceRecycler.setLayoutManager(manager);
        deviceRecycler.setAdapter(deviceAdapter);

        deviceAdapter.setListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceBean bean, String operate) {
                Esp8266ControlBean controlBean = new Esp8266ControlBean();
                controlBean.ip = bean.ip;
                controlBean.port = bean.port;
                controlBean.control = operate;

                DeviceOkSocketControl.getInstance(DeviceListActivity.this).sendControlMsg(controlBean);
            }
        });
    }

    private void initData() {
        deviceList = DBControl.quaryAll(this, DeviceBean.class);
        // 查询设备状态
        DeviceOkSocketControl.getInstance(this).setOnStatusListener(new DeviceOkSocketControl.OnStatusListener() {
            @Override
            public void onStatusCallback(String ip, String status) {
                for (DeviceBean deviceBean : deviceList) {
                    if (deviceBean.ip.equals(ip)) {
                        if ("open".equals(status) || "opened".equals(status)) {
                            deviceBean.status = "open";
                        } else if ("close".equals(status) || "closed".equals(status)) {
                            deviceBean.status = "close";
                        }
                        break;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onDisconnect(String ip) {
            }
        });

        for (DeviceBean deviceBean : deviceList) {
            Esp8266ControlBean controlBean = new Esp8266ControlBean();
            controlBean.ip = deviceBean.ip;
            controlBean.port = deviceBean.port;
            controlBean.control = "query\n\r";
            DeviceOkSocketControl.getInstance(this).sendControlMsg(controlBean);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeviceListActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMsg(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMsg.append(msg + "\n");
            }
        });
    }


    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("xunfei", msg);
        }
    }
}
