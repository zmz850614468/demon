package com.demon.remotecontrol.socketcontrol;

import android.util.Log;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;

/**
 * socketIO控制类
 */
public class SocketIoControl {

    private Socket socketIoClient;

    public void connect(String host) {
        IO.Options otps = new IO.Options();
        otps.transports = new String[]{Polling.NAME, WebSocket.NAME};
//        otps.transports = new String[]{Polling.NAME};
//        otps.transports = new String[]{WebSocket.NAME};
        try {
//            socketIoClient = IO.socket(host);
            socketIoClient = IO.socket(host, otps);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            showLog("host:" + host);
            showLog("初始化socketIo异常:" + e.getMessage());
            return;
        }

        if (socketIoClient != null) {
//            socketIoClient.io().on(Manager.EVENT_TRANSPORT, args -> {
//                Transport transport = (Transport) args[0];
//                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
//                    @Override
//                    public void call(Object... args) {
//                        Map<String, List<String>> mHeaders = (Map<String, List<String>>) args[0];
//                        mHeaders.put("Authorization", Arrays.asList("Basic bXl1c2VyOm15cGFzczEyMw=="));
//                    }
//                });
//            });
            socketIoClient.on(Socket.EVENT_CONNECT, args -> {
                if (onSocketIoListener != null) {
                    onSocketIoListener.onConnected();
                }
            });
            socketIoClient.on(Socket.EVENT_CONNECT_ERROR, args -> {
                if (onSocketIoListener != null) {
                    onSocketIoListener.onError(args[0].toString());
                }
            });
            socketIoClient.on(Socket.EVENT_DISCONNECT, args -> {
                if (onSocketIoListener != null) {
                    onSocketIoListener.onDisconnected();
                }
            });
            socketIoClient.on("message", args -> {
                if (onSocketIoListener != null) {
                    if (args != null && args.length > 0) {
                        if (args[0] instanceof String) {
                            onSocketIoListener.onReceiver((String) args[0]);
                        }
                    }
                }
            });
            socketIoClient.connect();
        }
    }

    /**
     * 是否发送数据成功
     *
     * @param msg
     * @return
     */
    public boolean sendMsg(String msg) {
        if (socketIoClient != null && socketIoClient.connected()) {
            socketIoClient.emit("androidEvent", msg);
            return true;
        }
        return false;
    }

    /**
     * 判断是否处于连接状态
     *
     * @return
     */
    public boolean isConnected() {
        if (socketIoClient != null) {
            return socketIoClient.connected();
        }
        return false;
    }

    public void close() {
        if (socketIoClient.connected()) {
            socketIoClient.close();
            socketIoClient = null;
        }
    }

    private OnSocketIoListener onSocketIoListener;

    public void setOnSocketIoListener(OnSocketIoListener onSocketIoListener) {
        this.onSocketIoListener = onSocketIoListener;
    }

    public interface OnSocketIoListener {

        void onConnected();

        void onDisconnected();

        void onError(String msg);

        void onReceiver(String data);

    }

    private void showLog(String msg) {
        Log.e("SocketIOControl", msg);
    }
}
