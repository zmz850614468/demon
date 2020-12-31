package com.lilanz.wificonnect.activitys;

import android.app.Activity;
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
import com.lilanz.wificonnect.adapters.DeviceAdapter;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceManagerActivity extends Activity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private String status;

    @BindView(R.id.rv_device)
    protected RecyclerView deviceRecycler;
    private DeviceAdapter deviceAdapter;
    private List<DeviceBean> deviceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
        ButterKnife.bind(this);

        initData();
        initAdapter();
        initService();
    }

    @OnClick(R.id.tv_add_device)
    public void onAddDeviceClicked(View v) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked(View v) {
        DBControl.deleteAll(this, DeviceBean.class, deviceList);
        deviceList.clear();
        deviceAdapter.notifyDataSetChanged();

        MsgBean msgBean = new MsgBean(MsgBean.DEVICE_REFRESH, "更新所有设备信息");
        AppDataControl.sendMsg(msgBean);
    }

    private void initService() {
        AppDataControl.addCallbackListener(callbackListener);
    }

    /**
     * 消息回调监听对象
     */
    private MsgCallbackListener callbackListener = new MsgCallbackListener() {
        @Override
        public void onControlCallback(DeviceControlBean bean) {
            super.onControlCallback(bean);

            for (DeviceBean deviceBean : deviceList) {
                if ("开关式".equals(deviceBean.controlType) && deviceBean.ip.equals(bean.ip)) {
                    deviceBean.status = bean.control;
                    break;
                }
            }
            DeviceManagerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    deviceAdapter.notifyDataSetChanged();
//                  showToast("设备控制成功！！");
                }
            });
        }

        @Override
        public void onDeviceRefresh(DeviceBean bean, boolean isLast) {
            super.onDeviceRefresh(bean, isLast);
            DBControl.createOrUpdate(DeviceManagerActivity.this, DeviceBean.class, bean);
            deviceList.add(bean);
            if (isLast) {
                DeviceManagerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("设备更新完成!!");
                        deviceAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onDeviceStatusUpdate(DeviceBean bean) {
            super.onDeviceStatusUpdate(bean);
            for (DeviceBean deviceBean : deviceList) {
                if (deviceBean.ip.equals(bean.ip)) {
                    deviceBean.status = bean.status;
                    DeviceManagerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                }
            }
        }
    };

    private void initAdapter() {
        deviceAdapter = new DeviceAdapter(this, deviceList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        deviceRecycler.setLayoutManager(manager);
        deviceRecycler.setAdapter(deviceAdapter);

        deviceAdapter.setListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeviceBean bean, String operate) {
                DeviceControlBean controlBean = new DeviceControlBean();
                controlBean.ip = bean.ip;
                controlBean.port = bean.port;
                controlBean.control = operate;

                MsgBean msgBean = new MsgBean(MsgBean.DEVICE_CONTROL, controlBean.toString());
                AppDataControl.sendMsg(msgBean);
            }
        });
    }

    private void initData() {
        deviceList = DBControl.quaryAll(this, DeviceBean.class);
        if (AppDataControl.wifiService != null) {
            for (DeviceBean deviceBean : deviceList) {
                if ("开关式".equals(deviceBean.controlType)) {
                    DeviceControlBean controlBean = new DeviceControlBean();
                    controlBean.ip = deviceBean.ip;
                    controlBean.port = deviceBean.port;
                    controlBean.control = "status";
                    MsgBean msgBean = new MsgBean(MsgBean.DEVICE_CONTROL, controlBean.toString());
                    AppDataControl.sendMsg(msgBean);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        AppDataControl.removeCallbackListener(callbackListener);
        super.onDestroy();
    }

    private void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeviceManagerActivity.this, msg, Toast.LENGTH_SHORT).show();
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
