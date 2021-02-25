package com.lilanz.wificonnect.activity_new;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.adapters.DeviceAdapter;
import com.lilanz.wificonnect.bean_new.Esp8266ControlBean;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.control_new.DeviceOkSocketControl;
import com.lilanz.wificonnect.control_new.DeviceVoiceControl;
import com.lilanz.wificonnect.controls.XunFeiVoiceControl;
import com.lilanz.wificonnect.daos.DBControl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeDeviceActivity extends Activity {

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
        // 打开讯飞语音识别
        handler.sendEmptyMessageDelayed(1, 1000);
//        if (App.isDebug) {
//            Esp8266ControlBean bean = DeviceVoiceControl.getInstance(this).parse(true, "打开灯");
//            if (bean != null) {
//                showLog(bean.toString());
//            }
//        }
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
            controlBean.control = deviceBean.getControlData(DeviceBean.STATUS_QUERY, null);
            DeviceOkSocketControl.getInstance(this).sendControlMsg(controlBean);
        }
    }

    @OnClick(R.id.iv_setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, AppSettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_search)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(this, SearchDeviceActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initAdapter() {
        deviceList = DBControl.quaryAll(this, DeviceBean.class);
        deviceAdapter = new DeviceAdapter(this, deviceList);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
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

                DeviceOkSocketControl.getInstance(HomeDeviceActivity.this).sendControlMsg(controlBean);
            }
        });
    }

    /**
     * 初始化讯飞语音
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:     // 打开讯飞语音监听
                    XunFeiVoiceControl.getInstance(HomeDeviceActivity.this).setOnOneShotResult(new XunFeiVoiceControl.OnOneShotResult() {
                        @Override
                        public void onResult(boolean status, String result) {
                            dealVoiceControl(status, result);

                            handler.sendEmptyMessageDelayed(1, 300);
                        }
                    });
                    XunFeiVoiceControl.getInstance(HomeDeviceActivity.this).oneShot();
                    break;
            }
        }
    };

    /**
     * 处理语音结果
     *
     * @param status
     * @param voiceResult
     */
    private void dealVoiceControl(boolean status, String voiceResult) {
        showLog("语音结果：" + status + " ; voiceResult: " + voiceResult);
        Esp8266ControlBean controlBean = DeviceVoiceControl.getInstance(this).parseVoiceResult(status, voiceResult);
        if (controlBean != null) {
            DeviceOkSocketControl.getInstance(this).sendControlMsg(controlBean);
        } else {
            // TODO 语音提示
        }
    }

    private void initData() {
        deviceList = DBControl.quaryAll(this, DeviceBean.class);
        // 查询设备状态
        DeviceOkSocketControl.getInstance(this).addListener(onStatusListener);

        for (DeviceBean deviceBean : deviceList) {
            Esp8266ControlBean controlBean = new Esp8266ControlBean();
            controlBean.ip = deviceBean.ip;
            controlBean.port = deviceBean.port;
            controlBean.control = deviceBean.getControlData(DeviceBean.STATUS_QUERY, null);
            DeviceOkSocketControl.getInstance(this).sendControlMsg(controlBean);
        }
    }

    private DeviceOkSocketControl.OnSocketListener onStatusListener = new DeviceOkSocketControl.OnSocketListener() {
        @Override
        public void onMsgCallback(String ip, String status) {
            for (DeviceBean deviceBean : deviceList) {
                if (deviceBean.ip.equals(ip)) {
                    if ("open".equals(status) || "opened".equals(status)) {
                        deviceBean.status = DeviceBean.STATUS_OPEN;
                    } else if ("close".equals(status) || "closed".equals(status)) {
                        deviceBean.status = DeviceBean.STATUS_CLOSE;
                    } else {
                        return;
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
    };

    @Override
    protected void onDestroy() {
        DeviceOkSocketControl.getInstance(this).removeListener(onStatusListener);
        super.onDestroy();
    }

    private void showToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HomeDeviceActivity.this, msg, Toast.LENGTH_SHORT).show();
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
