package demon.blecommunicate;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BleSocketThread extends Thread {
    private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    //    private static UUID uuid = UUID.fromString("00001124-0000-1000-8000-00805f9b34fb");
//    private static UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothServerSocket serverSocket;     // 服务器socket
    private BluetoothSocket bluetoothSocket;        // 蓝夜连接socket
    private BluetoothDevice bluetoothDevice;        // 蓝牙设备
    private BluetoothAdapter bluetoothAdapter;      // 蓝牙适配器
    private Handler handler;

    private SocketThread socketThread;

    private boolean isContinue;
    private int type;

    public BleSocketThread(BluetoothDevice device, Handler handler) {
        this.bluetoothDevice = device;
        this.handler = handler;
        type = 2;       // 蓝牙连接器设备
        for (ParcelUuid deviceUuid : device.getUuids()) {
            deviceUuid.getUuid();
            Log.e("TAG", "BleSocketThread: " + deviceUuid.getUuid());
        }
    }

    public BleSocketThread(BluetoothAdapter bluetoothAdapter, Handler handler) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.handler = handler;
        type = 1;       // 蓝牙服务器
        isContinue = true;
    }

    @Override
    public void run() {
        super.run();

        if (type == 1) {    // 服务器等待连接

            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("btspp", uuid);
            } catch (IOException e) {
                e.printStackTrace();
                sendMsg(3, "获取服务器socket异常");
                return;
            }

//            while (isContinue) {
            sendMsg(200, "等待蓝牙连接");
            try {
                bluetoothSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                sendMsg(202, "等待蓝牙连接异常");
                return;
            }

            socketThread = new SocketThread(bluetoothSocket, handler);
            socketThread.setType(type);
            socketThread.start();
//            }
        } else if (type == 2) {     // 客服端连接蓝牙服务器
            try {
//                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
                sendMsg(3, "连接蓝牙异常");
                return;
            }

//            sendMsg(1, "蓝牙连接成功");
            socketThread = new SocketThread(bluetoothSocket, handler);
            socketThread.setType(type);
            socketThread.start();
        }
    }

    /**
     * 关闭所有资源
     */
    public void close() {
        this.interrupt();
        isContinue = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                bluetoothSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if (socketThread != null) {
//            socketThread.close();
//            socketThread = null;
//        }
    }

    /**
     * 发送蓝牙信息
     *
     * @param msg
     */
    public void send(String msg) {
        if (socketThread != null) {
            socketThread.send(msg);
        }
    }

    /**
     * 发送蓝牙信息
     *
     * @param bytes
     */
    public void send(byte[] bytes) {
        if (socketThread != null) {
            socketThread.send(bytes);
        }
    }

    // ============================     信息监听器      ============================

    private void sendMsg(int what, String msg) {
        if (handler != null) {
            Message message = new Message();
            message.what = what;
            message.obj = msg;
            handler.sendMessage(message);
        }
//        if (listener != null) {
//            listener.onMsgReceiver(msg);
//        }
    }

    private OnMsgListener listener;

    public void setListener(OnMsgListener listener) {
        this.listener = listener;
    }

    public interface OnMsgListener {
        public void onMsgReceiver(String msg);
    }
}
