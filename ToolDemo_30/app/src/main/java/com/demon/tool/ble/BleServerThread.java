package com.demon.tool.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 蓝牙服务器类
 */
public class BleServerThread extends Thread {
    public static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //    private static UUID uuid = UUID.fromString("00001124-0000-1000-8000-00805f9b34fb");
//    private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothServerSocket serverSocket;     // 服务器socket
    private BluetoothSocket bluetoothSocket;        // 蓝牙连接socket
    private BluetoothAdapter bluetoothAdapter;      // 蓝牙适配器

    //    private BleSocketThread socketThread;
    private List<BleClientThread> bleSocketThreadList;

    private boolean isContinue;

    public BleServerThread() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isContinue = true;
        bleSocketThreadList = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();

        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("btspp", uuid);
        } catch (IOException e) {
            e.printStackTrace();
            if (serverListener != null) {
                serverListener.onError("初始化蓝牙服务器失败");
            }
            return;
        }
        if (serverListener != null) {
            serverListener.onConnected();
        }
        while (isContinue) {
            try {
                bluetoothSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                if (serverListener != null) {
                    serverListener.onError("蓝牙服务器等待连接异常");
                }
                close();
                return;
            }

            iniBleSocket(bluetoothSocket);
        }
    }

    /**
     * 初始化蓝牙客户端
     *
     * @param bluetoothSocket
     */
    private void iniBleSocket(BluetoothSocket bluetoothSocket) {
        BleClientThread socketThread = new BleClientThread(bluetoothSocket);
        socketThread.setOnBleClientListener(new BleClientThread.OnBleClientListener() {
            @Override
            public void onConnected(BluetoothSocket socket) {
                if (serverListener != null) {
                    serverListener.onBleSocketConnected(socket);
                }
                bleSocketThreadList.add(socketThread);
            }

            @Override
            public void onDisconnected(String deviceName, String deviceAddress) {
                if (serverListener != null) {
                    serverListener.onBleSocketDisconnected(deviceName, deviceAddress);
                }
                bleSocketThreadList.remove(socketThread);
            }

            @Override
            public void onError(String deviceName, String msg) {
                if (serverListener != null) {
                    serverListener.onBleSocketError(deviceName, msg);
                }
                socketThread.close();
            }

            @Override
            public void onReceiver(BluetoothSocket socket, String msg) {
                if (serverListener != null) {
                    serverListener.onBleSocketReceiver(socket, msg);
                }
            }
        });
        socketThread.start();
    }

    /**
     * 关闭所有资源
     */
    public void close() {
        this.interrupt();
        isContinue = false;
        for (BleClientThread bleSocketThread : bleSocketThreadList) {
            bleSocketThread.close();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (serverListener != null) {
                serverListener.onDisconnected();
            }
        }

//        if (bluetoothSocket != null) {
//            try {
//                bluetoothSocket.close();
//                bluetoothSocket = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 发送蓝牙信息
     *
     * @param msg
     */
    public void send(String msg) {
        for (BleClientThread bleSocketThread : bleSocketThreadList) {
            bleSocketThread.send(msg);
        }
//        if (socketThread != null) {
//            socketThread.send(msg);
//        }
    }

    // ============================     信息监听器      ============================

//    private void sendMsg(int what, String msg) {
//        if (handler != null) {
//            Message message = new Message();
//            message.what = what;
//            message.obj = msg;
//            handler.sendMessage(message);
//        }
//    }

    private OnBleServerListener serverListener;

    public void setServerListener(OnBleServerListener serverListener) {
        this.serverListener = serverListener;
    }

    public interface OnBleServerListener {

        void onConnected();

        void onDisconnected();

        void onError(String msg);

        void onBleSocketConnected(BluetoothSocket socket);

        void onBleSocketDisconnected(String deviceName, String deviceAddress);

        void onBleSocketError(String deviceName, String msg);

        void onBleSocketReceiver(BluetoothSocket socket, String msg);
    }

    private void showLog(String msg) {
        Log.e("BleServerThread", msg);
    }
}
