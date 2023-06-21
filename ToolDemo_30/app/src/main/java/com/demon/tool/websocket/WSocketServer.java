package com.demon.tool.websocket;

import android.util.Log;

import com.demon.tool.util.StringUtil;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class WSocketServer extends WebSocketServer {

    private List<WebSocket> webSocketList = new ArrayList<>();
//    private Handler msgHandle;

    public WSocketServer(int port) {
        super(new InetSocketAddress(port));
        onStart();
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        showLog("onOpen:" + conn.getRemoteSocketAddress());
        if (!webSocketList.contains(conn)) {
            webSocketList.add(conn);
        }
        showLog(2 + "socket 连接个数：" + webSocketList.size());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        showLog("onClose:" + conn.getRemoteSocketAddress());
        if (webSocketList.contains(conn)) {
            webSocketList.remove(conn);
        }
        showLog(2 + "socket 连接个数：" + webSocketList.size());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        showLog("onMessage:" + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        showLog("onError:" + ex.getMessage());
//        if (msgHandle != null) {
//            msgHandle.sendEmptyMessageDelayed(30, 3000);
//        }
        showLog(1 + "socket 开启错误：" + ex.getMessage());
        // todo 需要重新启动服务器
    }

    @Override
    public void onStart() {
        showLog("onStart");
        showLog(1 + "socket 开启成功");
    }

//    public void setMsgHandle(Handler msgHandle) {
//        this.msgHandle = msgHandle;
//    }

    private void showLog(String msg) {
        Log.e("WSocketServer", msg);
    }

    public void sendMsg(String msg) {
        showLog("连接设备数：" + webSocketList.size());
        StringBuffer buffer = new StringBuffer();
        buffer.append("时间：").append(StringUtil.getDataStr()).append("\n");
        if (webSocketList.isEmpty()) {
            buffer.append("没有设备可以发送数据：" + msg).append("\n");
            showLog(3 + buffer.toString());
            return;
        }

        for (WebSocket webSocket : webSocketList) {
            if (webSocket != null && webSocket.isOpen()) {
                buffer.append(webSocket.getRemoteSocketAddress().getAddress().getHostAddress())
                        .append(" : ").append(msg).append("\n");
                webSocket.send(msg);
            }
        }
        showLog(3 + buffer.toString());
    }

}

//    /**
//     * 开关webSocket服务器
//     */
//    public void switchWebSocket() {
//        if (wSocketServer == null) {
//            wSocketServer = new WSocketServer(18899);
//            wSocketServer.start();
//        } else {
//            try {
//                wSocketServer.stop();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            wSocketServer = null;
//        }
//    }
