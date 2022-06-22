package com.demon.fit.data_transfer.websocket;

import com.demon.fit.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class WSocketServer extends WebSocketServer {

    /**
     * 用于存储连接的webSocket客服端
     */
    private List<WebSocket> webSocketList = new ArrayList<>();
    private int port;
    private boolean isOpened;

    public WSocketServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
//        showLog("onOpen:" + conn.getRemoteSocketAddress());
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onWebSocketOpen(conn);
        }
        if (!webSocketList.contains(conn)) {
            synchronized (this) {
                webSocketList.add(conn);
            }
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        showLog("onClose:" + conn.getRemoteSocketAddress());
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onWebSocketClose(conn);
        }
        if (webSocketList.contains(conn)) {
            synchronized (this) {
                webSocketList.remove(conn);
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        if (onWebSocketServerListener != null) {
            try {
                MsgTypeBean bean = new Gson().fromJson(message, MsgTypeBean.class);
                onWebSocketServerListener.onWebSocketMsg(conn, bean.msgType, bean.msg);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                onWebSocketServerListener.onWebSocketMsg(conn, null, message);
            }

        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
//        showLog("onError:" + ex.getMessage());
        isOpened = false;
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onServerError(conn, ex);
        }
        // todo 报错后需要重连webSocket服务器
    }

    @Override
    public void onStart() {
//        showLog("onStart");
        isOpened = true;
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onServerStart(this.port);
        }
    }

    /**
     * 发送数据
     *
     * @param ip
     * @param port
     * @param msg
     */
    public void send(String ip, int port, String msg) {
        if (StringUtil.isEmpty(ip)) {
            for (WebSocket webSocket : webSocketList) {
                webSocket.send(msg);
            }
        } else {
            for (WebSocket webSocket : webSocketList) {
                if (webSocket.getRemoteSocketAddress().equals(ip)) {
                    webSocket.send(msg);
                    break;
                }
            }
        }
    }

//    private void showLog(String msg) {
//        Log.e("WSocketServer", msg);
//    }

    public boolean isOpened() {
        return isOpened;
    }

    private OnWebSocketServerListener onWebSocketServerListener;

    public void setOnWebSocketServerListener(OnWebSocketServerListener onWebSocketServerListener) {
        this.onWebSocketServerListener = onWebSocketServerListener;
    }

    public interface OnWebSocketServerListener {
        void onWebSocketOpen(WebSocket conn);

        void onWebSocketClose(WebSocket conn);

        void onWebSocketMsg(WebSocket conn, String msgType, String msg);

        void onServerStart(int port);

        void onServerError(WebSocket conn, Exception ex);
    }

}
