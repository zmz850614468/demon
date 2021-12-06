package com.demon.remotecontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.control.PermissionControl;
import com.demon.remotecontrol.interfaces.OnSocketStatusListener;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.demon.remotecontrol.util.AppUtil;
import com.demon.remotecontrol.util.SharePreferencesUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemoteActivity extends AppCompatActivity {

    @BindView(R.id.tv_socket_status)
    TextView tvSocketStatus;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        ButterKnife.bind(this);

        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.PHONE});
        initSocket();
        tvDeviceId.setText("设备号:" + App.deviceId);

        AppUtil.getInstallApp(this, false);
    }

    private void initSocket() {
        SocketMsgControl socketMsgControl = SocketMsgControl.getInstance(this);
        socketMsgControl.autoConnectSocket(App.host, App.deviceId);
        socketMsgControl.setOnDeviceNoCallback(deviceNo -> {
            showToast("没有找到对应设备：" + deviceNo);
        });

        socketMsgControl.setOnSocketStatusListener(new OnSocketStatusListener() {
            @Override
            public void onConnected() {
                tvSocketStatus.setText("socket连接成功");
            }

            @Override
            public void onDisconnected() {
                tvSocketStatus.setText("socket断开连接");
            }

            @Override
            public void onError(String errMsg) {
                tvSocketStatus.setText("socket连接异常");
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("RemoteActivity", msg);
    }
}
