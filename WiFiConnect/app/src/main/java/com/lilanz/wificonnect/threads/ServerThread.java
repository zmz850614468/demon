package com.lilanz.wificonnect.threads;

import com.lilanz.wificonnect.SocketThread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Wifi通讯的服务类
 */
public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private List<SocketThread> socketThreadList;

    private boolean isContinue = true;
    private int port;

    public ServerThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        super.run();

        try {
            serverSocket = new ServerSocket(port);
            if (listener != null) {
                listener.onReceiver("开启服务端口：" + port);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onReceiver("开启服务端口出错：" + port);
            }
            return;
        }
        while (isContinue) {
            try {
                Socket socket = serverSocket.accept();
                if (socketThreadList == null) {
                    socketThreadList = new ArrayList<>();
                }
                final SocketThread socketThread = new SocketThread();
                socketThreadList.add(socketThread);
                socketThread.startServer(socket);
                socketThread.setListener(new OnReceiverListener() {
                    @Override
                    public void onReceiver(SocketThread thread, String msg) {
                        super.onReceiver(thread, msg);
                        socketThreadList.remove(thread);
                        if (listener != null) {
                            listener.onReceiver(thread.getIp() + " " + msg);
                        }
                    }

                    @Override
                    public void onReceiver(String msg) {
                        if (listener != null) {
                            listener.onReceiver(socketThread.getIp() + ":" + msg);
//                            listener.onReceiver("收到消息:" + msg);
                        }
                    }
                });
                InetAddress dd = socket.getInetAddress();
                String add = dd.getHostAddress();
                socketThread.setIp(add);
                if (listener != null) {
                    listener.onReceiver(socket.getInetAddress() + "连接上");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onReceiver("服务端接收出现异常");
                }
                break;
            }
        }
    }

    /**
     * 向所有的断开发送信息
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        if (socketThreadList != null) {
            for (SocketThread socketThread : socketThreadList) {
                socketThread.sendMsg(msg);
            }
        }
    }

    /**
     * 关闭资源
     */
    public void close() {
        isContinue = false;
        if (socketThreadList != null) {
            for (SocketThread socketThread : socketThreadList) {
                socketThread.close();
            }
            socketThreadList.clear();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }
    }

    private OnReceiverListener listener;

    public void setListener(OnReceiverListener listener) {
        this.listener = listener;
    }
}
