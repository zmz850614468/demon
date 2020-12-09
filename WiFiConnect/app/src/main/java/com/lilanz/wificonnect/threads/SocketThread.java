package com.lilanz.wificonnect.threads;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lilanz.wificonnect.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

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
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } else if (type == 2) { // 客服端口
                socket = new Socket(ip, port);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                if (listener != null) {
                    listener.onTip(1, "连接服务端口：" + ip + "：" + port);
                    listener.onConnected(this, "连接服务器：" + ip + "：" + port);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onTip(2, "没有连接上目标" + ip + "：" + port);
                listener.onError(this, "没有连接上目标");
            }
            return;
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024 * 5);
        while (isContinue) {
            byte[] buffer = new byte[102400];
            int count = 0;
            try {
                count = inputStream.read(buffer);
                if (count == -1) {
                    if (listener != null) {
                        listener.onError(this, "断开端口连接");
                    }
                    close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (count > 0) {
                byteBuffer.put(buffer, 0, count);
            }
            // TODO 数据的接收需要修改
            if (count > 0 && count < 102400) {
                if (listener != null) {
                    int position = byteBuffer.position();
                    byte[] result = new byte[position];
                    byteBuffer.flip();
                    byteBuffer.get(result);
                    byteBuffer.flip();
                    List<String> msgList = StringUtil.getSpliteMsg(new String(result));
                    for (String s : msgList) {
                        if (!StringUtil.isEmpty(s)) {
                            listener.onReceiver(s);
                            Log.e(TAG, position + " reseace msg:" + s);
                        }
                    }
                    byteBuffer.clear();
                }
            }
        }
    }

    public static final String SPLIT = ".-_-.";

    /**
     * 发送信息，有对信息进行处理
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        try {   //发送
            outputStream.write((msg + SPLIT).getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送信息，没有对信息进行处理
     *
     * @param bytes
     */
    public void sendMsg(byte[] bytes) {
        try {   //发送
            outputStream.write(bytes);
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
        if (listener != null) {
            listener.onTip(2, "断开连接");
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private OnReceiverListener listener;

    public void setListener(OnReceiverListener listener) {
        this.listener = listener;
    }


}
