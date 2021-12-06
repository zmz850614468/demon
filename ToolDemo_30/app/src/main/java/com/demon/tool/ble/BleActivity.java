package com.demon.tool.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.controls.PermissionControl;
import com.demon.tool.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleActivity extends AppCompatActivity {

    @BindView(R.id.tv_selected_ble)
    TextView tvSelectedBleName;

    private BleServerThread bleServerThread;
    private BleClientThread bleClientThread;
    private BluetoothAdapter bluetoothAdapter;

    private BleDialog bleDialog;
    private String selectedBleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        ButterKnife.bind(this);

        initUI();

        new PermissionControl(this).locationPermission();
        new PermissionControl(this).blePermission();
    }

    @OnClick({R.id.bt_open_server, R.id.bt_close_server, R.id.bt_connect, R.id.bt_disconnect,
            R.id.bt_select_ble})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_open_server:
                startBleServer();

                break;
            case R.id.bt_close_server:
                if (bleServerThread != null) {
                    bleServerThread.close();
                    bleServerThread = null;
                } else {
                    showLog("蓝牙服务器已经关闭");
                }
                break;

            case R.id.bt_connect:
                if (!StringUtil.isEmpty(selectedBleName)) {
                    if (bleClientThread == null) {
                        startBleClient();
                    } else {
                        showToast("已连接的蓝牙设备");
                    }
                } else {
                    showLog("请先选择要连接的蓝牙设备");
                    showToast("请先选择要连接的蓝牙设备");
                }
                break;

            case R.id.bt_disconnect:
                if (bleClientThread != null) {
                    bleClientThread.close();
                    bleClientThread = null;
                } else {
                    showLog("已连接蓝牙设备:" + selectedBleName);
                }
                break;

            case R.id.bt_select_ble:
                Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
                List<String> bleNameList = new ArrayList<>();
                if (set != null) {
                    for (BluetoothDevice bluetoothDevice : set) {
                        bleNameList.add(bluetoothDevice.getName());
                    }
                }
                bleDialog.updateData(bleNameList, selectedBleName);
                bleDialog.show();
                break;
        }
    }

    /**
     * 开启蓝牙服务器
     */
    private void startBleServer() {
        if (bleServerThread == null) {
            bleServerThread = new BleServerThread();
            bleServerThread.setServerListener(new BleServerThread.OnBleServerListener() {
                @Override
                public void onConnected() {
                    showLog("蓝牙服务器开启成功");
                }

                @Override
                public void onDisconnected() {
                    showLog("蓝牙服务器关闭");
                    bleServerThread = null;
                }

                @Override
                public void onError(String msg) {
                    if (bleServerThread != null) {
                        bleServerThread.close();
                    }
                    bleServerThread = null;
                    showLog(msg);
                }

                @Override
                public void onBleSocketConnected(BluetoothSocket socket) {
                    showLog("有蓝牙设备连接：" + socket.getRemoteDevice().getName());
                }

                @Override
                public void onBleSocketDisconnected(String deviceName, String deviceAddress) {
                    showLog("有蓝牙设备断开连接：" + deviceName);
                }

                @Override
                public void onBleSocketError(String deviceName, String msg) {
                    showLog("蓝牙异常：" + deviceName + " : " + msg);
                }

                @Override
                public void onBleSocketReceiver(BluetoothSocket socket, String msg) {
                    showLog("接收到蓝牙数据：" + socket.getRemoteDevice().getName() + ": " + msg);
                }
            });
            bleServerThread.start();
        } else {
            showLog("蓝牙服务器已经开启");
        }
    }

    private void startBleClient() {
        Set<BluetoothDevice> set = bluetoothAdapter.getBondedDevices();
        if (set != null) {
            for (BluetoothDevice bluetoothDevice : set) {
                if (selectedBleName.equals(bluetoothDevice.getName())) {
                    bleClientThread = new BleClientThread(selectedBleName, bluetoothDevice.getAddress());
                    bleClientThread.setOnBleClientListener(new BleClientThread.OnBleClientListener() {
                        @Override
                        public void onConnected(BluetoothSocket socket) {
                            showLog("连接蓝牙设备成功");
                        }

                        @Override
                        public void onDisconnected(String deviceName, String deviceAddress) {
                            showLog("断开蓝牙设备连接");
                            bleClientThread.close();
                            bleClientThread = null;
                        }

                        @Override
                        public void onError(String deviceName, String msg) {
                            if (bleClientThread != null) {
                                bleClientThread.close();
                            }
                            bleClientThread = null;
                            showLog("蓝牙客服端异常：" + deviceName + " ; " + msg);
                        }

                        @Override
                        public void onReceiver(BluetoothSocket socket, String msg) {
                            showLog("接收到设备发来的数据：" + socket.getRemoteDevice().getName() + " : " + msg);
                        }
                    });
                    bleClientThread.start();
                    break;
                }
            }
        }
    }

    private void initUI() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        bleDialog = new BleDialog(this, R.style.DialogStyleOne);
        bleDialog.setOnBleSelectedListener(msg -> {
            selectedBleName = msg;
            tvSelectedBleName.setText(msg);
        });
        bleDialog.show();
        bleDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bleServerThread != null) {
            bleServerThread.close();
            bleServerThread = null;
        }
        if (bleClientThread != null) {
            bleClientThread.close();
            bleClientThread = null;
        }
    }

    private void showLog(String msg) {
        Log.e("BleActivity", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
