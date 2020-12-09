package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.threads.WifiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceManagerActivity extends Activity {

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    private WifiService wifiService;

    private String status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
        ButterKnife.bind(this);

        initService();

    }

    @OnClick({R.id.bt_open, R.id.bt_close, R.id.bt_status})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                status = "open";
                break;
            case R.id.bt_close:
                status = "close";
                break;
            case R.id.bt_status:
                status = "status";
                break;
        }

        if (wifiService != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    DeviceControlBean bean = new DeviceControlBean();
                    bean.ip = "192.168.1.114";
                    bean.port = 80;
                    bean.control = status;


                    MsgBean msgBean = new MsgBean(MsgBean.DEVICE_CONTROL, bean.toString());
                    wifiService.sendMsg(msgBean.toString());
                }
            }.start();
        }
    }

    @OnClick(R.id.tv_add_device)
    public void onAddDeviceClicked(View v) {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }

    private void initService() {
        Intent intent = new Intent(this, WifiService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wifiService = ((WifiService.WifiBind) service).getService();
            wifiService.addMsgCallBackListener(callbackListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wifiService = null;
        }
    };

    /**
     * 消息回调监听对象
     */
    private MsgCallbackListener callbackListener = new MsgCallbackListener() {
    };

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
