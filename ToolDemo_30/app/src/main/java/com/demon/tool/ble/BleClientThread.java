package com.demon.tool.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 蓝牙客户端类
 */
public class BleClientThread extends Thread {

    private String deviceName;
    private String deviceAddress;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothDevice bleDevice;

    private boolean isContinue;

    public BleClientThread(@NonNull BluetoothSocket socket) {
        this.socket = socket;
        isContinue = true;
        deviceName = this.socket.getRemoteDevice().getName();
        deviceAddress = this.socket.getRemoteDevice().getAddress();
    }

    public BleClientThread(@NonNull String deviceName, @NonNull String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bleDevice = bluetoothAdapter.getRemoteDevice(this.deviceAddress);
        isContinue = true;
    }

    @Override
    public void run() {
        super.run();

        if (socket == null) {
            try {
                this.socket = bleDevice.createInsecureRfcommSocketToServiceRecord(BleServerThread.uuid);
            } catch (IOException e) {
                e.printStackTrace();
                if (onBleClientListener != null) {
                    onBleClientListener.onError(deviceName, "初始化 BluetoothSocket 异常");
                }
                return;
            }
        }

        try {
            if (!socket.isConnected()) {
                socket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onBleClientListener != null) {
                onBleClientListener.onError(deviceName, "蓝牙发起连接异常：" + e.toString());
            }
            return;
        }

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            if (onBleClientListener != null) {
                onBleClientListener.onError(deviceName, "获取输入输出流异常:" + e.toString());
            }
            return;
        }
        if (onBleClientListener != null) {
            onBleClientListener.onConnected(socket);
        }

        byte[] bytes = new byte[1024];
        while (isContinue) {
            try {
                int count = inputStream.read(bytes);
                if (count < 0) {
                    showLog("读取数据长度 < 0");
                    break;
                }
                if (onBleClientListener != null) {
                    onBleClientListener.onReceiver(socket, new String(bytes, 0, count));
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (onBleClientListener != null) {
                    onBleClientListener.onError(deviceName, "读取蓝牙数据异常：" + e.toString());
                }
                break;
            }
        }
    }

    /**
     * 发送蓝牙信息
     *
     * @param msg
     */
    public void send(String msg) {
        try {
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            if (onBleClientListener != null) {
                onBleClientListener.onError(deviceName, "蓝牙写数据异常：" + e.toString());
            }
        }
    }

    public void close() {
        isContinue = false;
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            if (onBleClientListener != null) {
                onBleClientListener.onDisconnected(deviceName, deviceAddress);
            }
        }
    }

    private OnBleClientListener onBleClientListener;

    public void setOnBleClientListener(OnBleClientListener onBleClientListener) {
        this.onBleClientListener = onBleClientListener;
    }

    public interface OnBleClientListener {

        void onConnected(BluetoothSocket socket);

        void onDisconnected(String deviceName, String deviceAddress);

        void onError(String deviceName, String msg);

        void onReceiver(BluetoothSocket socket, String msg);
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    private void showLog(String msg) {
        Log.e("BleSocketThread", msg);
    }

}
