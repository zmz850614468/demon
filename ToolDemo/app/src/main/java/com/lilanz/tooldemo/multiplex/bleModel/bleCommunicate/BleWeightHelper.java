package com.lilanz.tooldemo.multiplex.bleModel.bleCommunicate;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.Toast;

import com.lilanz.tooldemo.utils.BuildUtil;
import com.lilanz.tooldemo.utils.StringUtil;

/**
 * 蓝牙 称重
 */
public class BleWeightHelper {

    private Context context;
    private WebView webView;

    private BluetoothAdapter bluetoothAdapter;
    private BleSocketThread bleSocketThread;
    private boolean isDebug;


    public BleWeightHelper(Context context, WebView webView) {
        isDebug = BuildUtil.isApkInDebug(context);
        this.context = context;
        this.webView = webView;
        initBle();
    }

    private void initBle() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            showToast("不支持蓝牙设备");
            return;
        }

        // 开启蓝牙
        if (!bluetoothAdapter.isEnabled() && context instanceof Activity) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity) context).startActivityForResult(enableBtIntent, 2);
        }

        // 注册广播接收器，以获取蓝牙设备搜索结果
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
    }

    /**
     * 开启蓝牙扫描
     */
    public void startScanBle() {
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        } else {
            showToast("正在进行蓝牙搜索，请等待");
        }
    }

    /**
     * 停止蓝牙扫描
     */
    public void stopScanBle() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * 连接蓝牙地址
     *
     * @param address 84:48:1E:05:94:54
     */
    public void startConnectBle(String address) {
        if (StringUtil.isEmpty(address)) {
            showToast("蓝牙地址不能为空");
            return;
        }

        stopScanBle();  // 连接蓝牙前要结束蓝牙扫描

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        bleSocketThread = new BleSocketThread(device, handler);
        bleSocketThread.start();
    }

    /**
     * 蓝牙搜索监听广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // TODO 搜索获取的蓝牙设备
//                jsInterface.Ble_Device(device.getName(), device.getAddress());
//                String address = SharePreferencesUtil.getBleAddress(context);
//                if (address.equals(device.getAddress())) {
//                    showToast("查找到匹配蓝牙");
//                    startConnectBle(address);
//                }
            }
        }
    };

    private Handler handler = new Handler() {
        private StringBuffer buffer = new StringBuffer();

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:// 显示信息
                    showToast("提示:" + msg.obj.toString());
                    break;
                case 2:
                    if (buffer.length() == 0) {
                        webView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isDebug) {
                                    showToast("接收:" + buffer.toString());
                                }
                                // TODO 处理蓝牙设备发来的数据
                                if (buffer.length() > 0) {
                                    buffer.delete(0, buffer.length());
                                }
                            }
                        }, 500);
                    }
                    buffer.append(msg.obj.toString());
                    break;
                case 3:
                    showToast("异常:" + msg.obj.toString());
                    if (bleSocketThread != null) {
                        bleSocketThread.close();
                        bleSocketThread = null;
                    }
                    break;
                case 11: // 成功连接蓝牙设备
                    if (isDebug) {
                        showToast("成功连接蓝牙设备");
                    }
                    break;
                case 12:  // 断开蓝牙连接
                    if (isDebug) {
                        showToast("断开蓝牙连接");
                    }
                    break;
            }
        }
    };

    /**
     * 关闭资源
     */
    public void close() {
        if (bleSocketThread != null) {
            bleSocketThread.close();
            bleSocketThread = null;
        }
        context.unregisterReceiver(mReceiver);
    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
