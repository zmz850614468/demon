package com.lilanz.wificonnect;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lilanz.wificonnect.threads.OnReceiverListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketThread extends Thread {

    private String TAG = "SocketThread";

    //    private ServerSocket serverSocket;
    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean isContinue = true;
    private String ip;
    private int port;

    /**
     * 1:服务端口
     * 2:客服端口
     */
    private int type;

    public SocketThread() {
    }

    /**
     * 开启服务端口
     */
    @Deprecated
    public void startService(int port) {
        type = 1;
        this.port = port;
        this.start();
    }

    public void startServer(@NonNull Socket socket) {
        this.type = 1;
        this.socket = socket;
        this.start();
    }

    public void startConnect(String ip, int port) {
        type = 2;
        this.ip = ip;
        this.port = port;
        this.start();
    }

    @Override
    public void run() {
        super.run();

        try {
            if (type == 1) {
                // 服务器端口
            } else if (type == 2) { // 客服端口
                socket = new Socket(ip, port);
                if (listener != null) {
                    listener.onReceiver("连接服务端口：" + ip + "：" + port);
                }
            }
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onReceiver(this,"连接出错");
            }
            return;
        }

        while (isContinue) {
            byte[] buffer = new byte[1024];
            int count = 0;
            try {
                count = inputStream.read(buffer);
                if (count == -1) {
                    if (listener != null) {
                        listener.onReceiver(this, "断开端口连接");
                    }
                    close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (count > 0) {
                byte[] receive = new byte[count];
                for (int i = 0; i < count; i++) {
                    receive[i] = buffer[i];
                }

                if (listener != null) {
                    listener.onReceiver(new String(receive));
                }
                Log.e(TAG, "reseace msg:" + new String(receive));
            }
        }
    }

    public void sendMsg(String msg) {
        try {   //发送
            outputStream.write(msg.getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭服务端口
     */
    public void close() {
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream != null) {
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isContinue = false;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private OnReceiverListener listener;

    public void setListener(OnReceiverListener listener) {
        this.listener = listener;
    }


}
