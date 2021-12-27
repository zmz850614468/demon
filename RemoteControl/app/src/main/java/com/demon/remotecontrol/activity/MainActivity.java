package com.demon.remotecontrol.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.control.PermissionControl;
import com.demon.remotecontrol.dialog.DeviceDialog;
import com.demon.remotecontrol.dialog.ProgressDialog;
import com.demon.remotecontrol.interfaces.OnFileDeliverCallback;
import com.demon.remotecontrol.interfaces.OnSocketStatusListener;
import com.demon.remotecontrol.socketcontrol.SocketBean;
import com.demon.remotecontrol.socketcontrol.SocketMsgControl;
import com.demon.remotecontrol.util.DeviceUtils;
import com.demon.remotecontrol.util.StringUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_socket_status)
    TextView tvSocketStatus;

    private DeviceDialog deviceDialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.PHONE});
        initSocket();
        initDialog();
    }

    @OnClick({R.id.bt_get_all_app, R.id.bt_delete_app, R.id.bt_fiel_viewer})
    public void onClicked(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_get_all_app:
                intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_fiel_viewer:
//                intent = new Intent(this, LocalFileViewActivity.class);
                intent = new Intent(this, RemoteFileViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_delete_app:
//                showToast("删除应用：com.lilanz.login");
//                AppUtil.uninstall(this, "com.lilanz.login");
                break;

        }
    }


    @OnClick({R.id.bt_remote_app, R.id.bt_all_device, R.id.bt_local_file, R.id.bt_remote_file, R.id.bt_progress})
    public void onFunctionClicked(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_all_device:
                queryAllDevice();
                break;
            case R.id.bt_remote_app:
                if (StringUtil.isEmpty(App.selectedDevice)) {
                    showToast("请先选择远程设备");
                    break;
                }
                intent = new Intent(this, AppListActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_remote_file:
                if (StringUtil.isEmpty(App.selectedDevice)) {
                    showToast("请先选择远程设备");
                    break;
                }
                intent = new Intent(this, RemoteFileViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_local_file:
                if (StringUtil.isEmpty(App.selectedDevice)) {
                    showToast("请先选择远程设备");
                    break;
                }
                intent = new Intent(this, LocalFileViewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_progress:
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                } else {
                    progressDialog.show();
                }
                break;
        }
    }

    private void initSocket() {
        SocketMsgControl socketMsgControl = SocketMsgControl.getInstance(this);
        socketMsgControl.setOnDeviceNoCallback(deviceNo -> {
            showToast("没有找到对应设备：" + deviceNo);
        });
        socketMsgControl.setOnAllDeviceNoCallback(deviceNoList -> {
            deviceNoList.remove(App.deviceId);
            if (deviceNoList.isEmpty()) {
                showToast("没有找到其他设备连接");
                return;
            }
            deviceDialog.updateData(deviceNoList, App.selectedDevice);
            deviceDialog.show();
//            showLog("获取所有设备信息：" + new Gson().toJson(deviceNoList));
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

        socketMsgControl.autoConnectSocket(App.host, App.deviceId);
    }

    /**
     * 查询所有设备信息
     */
    private void queryAllDevice() {
        SocketBean bean = new SocketBean();
        bean.from = App.deviceId;
        bean.to = SocketBean.DEVICE_SERVER;
        bean.command = SocketBean.SOCKET_TYPE_QUERY_ALL_DEVICE;

        SocketMsgControl.getInstance(this).sendMsg(new Gson().toJson(bean));
    }

    private void initDialog() {
        deviceDialog = new DeviceDialog(this, R.style.DialogStyleOne);
        deviceDialog.setOnDeviceSelectedListener(msg -> {
            App.selectedDevice = msg;
        });
        deviceDialog.show();
        deviceDialog.dismiss();

        progressDialog = new ProgressDialog(this, R.style.DialogStyleOne);
        progressDialog.show();
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketMsgControl.getInstance(this).close();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}
