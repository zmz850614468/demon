package com.demon.tool.data_transfer.websocket;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * webSocket 客户端
 */
public class WSocketClient extends WebSocketClient {

    private static final int DELAY_TIME = 5000;
    private boolean isDestroy;

    public WSocketClient(URI serverUri) {
        super(serverUri);
        hearBitHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        showLog("onOpen: 打开webSocket");
        if (onWebSocketClientListener != null) {
            onWebSocketClientListener.onOpen();
        }
    }

    @Override
    public void onMessage(String message) {
//        showLog("onMessage: 接收webSocket信息：" + message);
        if (onWebSocketClientListener != null) {
            onWebSocketClientListener.onMessage(message);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
//        showLog("onClose(...): 关闭webSocket");
        if (onWebSocketClientListener != null) {
            onWebSocketClientListener.onClose();
        }
    }

    @Override
    public void onError(Exception ex) {
//        showLog("onError: " + ex.getMessage());
        ex.printStackTrace();
        if (onWebSocketClientListener != null) {
            onWebSocketClientListener.onError(ex);
        }
    }

    public void onDestroy() {
        isDestroy = true;
        hearBitHandler.removeMessages(1);
        close();
    }

//    private void showLog(String msg) {
//        Log.e("webSocket", msg);
//    }

    private Handler hearBitHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            showLog("发送心跳包:");
            if (isDestroy) {
                if (!isClosed()) {
                    onDestroy();
                }
                return true;
            }
            if (isClosed()) {
                reconnectWs();
            }
            //定时对长连接进行心跳检测
            hearBitHandler.sendEmptyMessageDelayed(1, DELAY_TIME);
            return true;
        }
    });

    /**
     * 开启重连
     */
    private void reconnectWs() {
        new Thread() {
            @Override
            public void run() {
                try {
                    //重连
                    reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        showLog("重连webSocket服务器");
    }

    private OnWebSocketClientListener onWebSocketClientListener;

    public void setOnWebSocketClientListener(OnWebSocketClientListener onWebSocketClientListener) {
        this.onWebSocketClientListener = onWebSocketClientListener;
    }

    public interface OnWebSocketClientListener {
        void onOpen();

        void onClose();

        void onError(Exception ex);

        void onMessage(String msg);
    }

}
