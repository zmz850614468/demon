package com.demon.tool.data_transfer.websocket;

import android.util.Log;

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

    public WSocketServer(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        showLog("onOpen:" + conn.getRemoteSocketAddress());
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onWebSocketOpen(conn);
        }
        if (!webSocketList.contains(conn)) {
            webSocketList.add(conn);
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        showLog("onClose:" + conn.getRemoteSocketAddress());
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onWebSocketClose(conn);
        }
        if (webSocketList.contains(conn)) {
            webSocketList.remove(conn);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        showLog("onMessage:" + message);
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onWebSocketMsg(conn, message);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        showLog("onError:" + ex.getMessage());
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onServerError(conn, ex);
        }
        // todo 报错后需要重连webSocket服务器
    }

    @Override
    public void onStart() {
        showLog("onStart");
        if (onWebSocketServerListener != null) {
            onWebSocketServerListener.onServerStart(this.port);
        }
    }

    private void showLog(String msg) {
        Log.e("WSocketServer", msg);
    }

    private OnWebSocketServerListener onWebSocketServerListener;

    public void setOnWebSocketServerListener(OnWebSocketServerListener onWebSocketServerListener) {
        this.onWebSocketServerListener = onWebSocketServerListener;
    }

    public interface OnWebSocketServerListener {
        void onWebSocketOpen(WebSocket conn);

        void onWebSocketClose(WebSocket conn);

        void onWebSocketMsg(WebSocket conn, String msg);

        void onServerStart(int port);

        void onServerError(WebSocket conn, Exception ex);
    }

}
