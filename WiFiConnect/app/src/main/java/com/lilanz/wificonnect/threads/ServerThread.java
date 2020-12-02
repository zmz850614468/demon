package com.lilanz.wificonnect.threads;

import android.media.MediaPlayer;
import android.os.Handler;

import com.lilanz.wificonnect.activitys.App;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.controls.AppDataControl;
import com.lilanz.wificonnect.controls.MediaControl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

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
                listener.onTip(3, "开启服务端口：" + port);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onTip(4, "开启服务端口出错：" + port);
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
                    public void onTip(int type, String msg) {
                        if (listener != null) {
                            listener.onTip(type, socketThread.getIp() + ":" + msg);
                        }
                    }

                    @Override
                    public void onError(SocketThread thread, String msg) {
                        super.onError(thread, msg);
                        socketThreadList.remove(thread);
                        if (listener != null) {
                            listener.onError(thread, msg);
                        }
                    }

                    @Override
                    public void onReceiver(String msg) {
                        if (listener != null) {
                            listener.onReceiver(socketThread, msg);
//                            listener.onReceiver("收到消息:" + msg);
//                            if (App.isDebug) {
                            socketThread.sendMsg(new MsgBean(2, "服务器收到消息！！").toString());
//                            }
                        }
                    }
                });
                InetAddress dd = socket.getInetAddress();
                String add = dd.getHostAddress();
                socketThread.setIp(add);
                if (listener != null) {
                    listener.onTip(1, socket.getInetAddress() + "连接上");
                }
                Thread.sleep(300);
                sendConnectedInfo(socketThread);
            } catch (Exception e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onTip(0, "服务端接收出现异常");
                }
//                break;
            }
        }
    }

    /**
     * 连接上后，发送一些必须的信息
     */
    private void sendConnectedInfo(SocketThread socketThread) {
        // 歌曲播放模式
        MsgBean msgBean = new MsgBean(MsgBean.MUSIC_PLAY_MODE, AppDataControl.playMode + "");
        socketThread.sendMsg(msgBean.toString());

        // 目前正在播放的音乐信息
        msgBean = MediaControl.getInstance(App.context).getPlayingInfo();
        if (msgBean != null) {
            socketThread.sendMsg(msgBean.toString());
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
        if (listener != null) {
            listener.onTip(2, "断开连接");
        }
    }

    private OnReceiverListener listener;

    public void setListener(OnReceiverListener listener) {
        this.listener = listener;
    }
}
