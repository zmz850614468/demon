package demon.blecommunicate;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SocketThread extends Thread {

    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler handler;
    private int type; // 1:服务端  ； 2：客服端

    private boolean isContinue;

    public SocketThread(@NonNull BluetoothSocket socket, Handler handler) {
        this.socket = socket;
        this.handler = handler;
        isContinue = true;
    }

    @Override
    public void run() {
        super.run();

        try {
            if (socket.isConnected()) {
                sendMsg(1, "蓝牙设备已经连接了");
            } else {
                socket.connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMsg(3, "socket连接异常" + e.toString());
            return;
        }

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            if (type == 2) {
                sendMsg(201, "蓝牙连接成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMsg(3, "获取输入输出流异常:" + e.toString());
            return;
        }
//        sendMsg(1, "等待发送和接收数据");
        byte[] bytes = new byte[1024];
        while (isContinue) {
            try {
                int count = inputStream.read(bytes);
                if (count < 0) {
                    sendMsg(3, "断开蓝牙连接");
                    break;
                }
                receiveMsg(new String(bytes, 0, count));
            } catch (Exception e) {
                e.printStackTrace();
                sendMsg(3, "读取蓝牙数据异常：" + e.toString());
                break;
            }
        }
        close();
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
            sendMsg(3, "蓝牙写数据出错：" + e.toString());
        }
    }

    /**
     * 发送蓝牙信息
     *
     * @param bytes
     */
    public void send(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            sendMsg(3, "蓝牙写数据出错：" + e.toString());
        }
    }

    /**
     * 接收信息
     *
     * @param msg
     */
    public void receiveMsg(String msg) {
        if (handler != null) {
            Message message = new Message();
            message.what = 2;
            message.obj = msg;
            handler.sendMessage(message);
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
        }
    }

    /**
     * 显示提示信息
     *
     * @param msg
     */
    private void sendMsg(int what, String msg) {
        if (handler != null) {
            Message message = new Message();
            message.what = what;
            message.obj = msg;
            handler.sendMessage(message);
        }
    }

    public void setType(int type) {
        this.type = type;
    }
}
