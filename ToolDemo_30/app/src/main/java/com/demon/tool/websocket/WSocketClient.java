package com.demon.tool.websocket;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.demon.tool.util.SharePreferencesUtil;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * webSocket 客户端
 */
public class WSocketClient extends WebSocketClient {
    private WebSocketCallback webSocketCallback;
    private String socketAddr;
    private boolean connected = false;
    private boolean isDestroy = false;

    public WSocketClient(String socketAddr, WebSocketCallback webSocketCallback) {
        super(URI.create(socketAddr));
        this.socketAddr = socketAddr;
        this.webSocketCallback = webSocketCallback;
        connect();
    }

    private Handler reConnectHandle = new Handler(Looper.myLooper(), msg -> {
        reconnect();
        return true;
    });

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        showLog("onOpen: 打开webSocket");
        connected = true;
        if (webSocketCallback != null) {
            webSocketCallback.onOpen();
        }
        connected = true;
    }

    @Override
    public void onMessage(String message) {
        showLog("onMessage: 接收webSocket信息：" + message);
        if (webSocketCallback != null) {
            webSocketCallback.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        showLog("onClose: 关闭webSocket");
        connected = false;
        if (webSocketCallback != null) {
            webSocketCallback.onClosed();
        }
        if (!isDestroy) {
            reConnectHandle.sendEmptyMessageDelayed(1, 3000);
        }
    }

    @Override
    public void connect() {
        super.connect();
        showLog("connect " + socketAddr);
    }

    @Override
    public void onError(Exception ex) {
        showLog("onError: " + ex.getMessage());
        connected = false;
        if (webSocketCallback != null) {
            webSocketCallback.onFailure(ex.getMessage());
        }
    }

    private void showLog(String msg) {
        Log.e("webSocket", msg);
    }

    public void onDestroy() {
        reConnectHandle.removeMessages(1);
        isDestroy = true;
        close();
    }

    public void setWebSocketCallback(WebSocketCallback webSocketCallback) {
        this.webSocketCallback = webSocketCallback;
    }

    /**
     * 只暴露需要的回调给页面，onFailure 你给了页面，页面也无能为力不知怎么处理
     */
    public interface WebSocketCallback {
        void onMessage(String text);

        void onOpen();

        void onClosed();

        void onFailure(String failure);
    }

}

// 调用方法
//    public void connectWebSocket(String webSocketAddr) {
//        SharePreferencesUtil.saveWebSocketAddr(this, webSocketAddr);
//        wSocketClient.onDestroy();
//        initSocket();
//    }
//
//    private void initSocket() {
//        String webSocketAddr = SharePreferencesUtil.getWebSocketAddr(this);
//        wSocketClient = new WSocketClient(webSocketAddr, new WSocketClient.WebSocketCallback() {
//            @Override
//            public void onMessage(String text) {
//                // {"mailno":"","weight":,"token":""}
//                showLog("gson数据：" + text);
//                MsgBean bean = null;
//                try {
//                    bean = new Gson().fromJson(text, MsgBean.class);
//                    deliverList.add(0, bean.mailno);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(() -> showToast("gson数据出错：" + text));
//                }
//                if (bean != null) {
////                    runOnUiThread(() -> expressSortingUi.refreshAdapter());
//                    deliverRequestControl.requestDeliverType(bean);
//                }
//            }
//
//            @Override
//            public void onOpen() {
//                runOnUiThread(() -> expressSortingUi.changeWebSocketStatus("webSocket连接成功"));
//            }
//
//            @Override
//            public void onClosed() {
//                runOnUiThread(() -> expressSortingUi.changeWebSocketStatus("webSocket已关闭"));
//            }
//
//            @Override
//            public void onFailure(String failure) {
//                runOnUiThread(() -> expressSortingUi.changeWebSocketStatus("webSocket连接失败"));
//            }
//        });
//    }
