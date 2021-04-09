package com.lilanz.tooldemo.multiplex.websocket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lilanz.tooldemo.R;

import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketActivity extends AppCompatActivity {

//    private static final String WS = "ws://192.168.1.102:8899";
    private static final String WS = "http://192.168.37.43:9092?mac=5";
//    private static final String WS = "192.168.1.136:8899";

    private WebSocket webSocket;
    private WebSocketCallback webSocketCallback;
    private int reconnectTimeout = 5000;
    private boolean connected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        ButterKnife.bind(this);

        connectSocket();
    }

    private class WebSocketHandler extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            showLog("onOpen");
            if (webSocketCallback != null) {
                webSocketCallback.onOpen();
            }
            connected = true;
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            showLog("onMessage ：" + text);
            if (webSocketCallback != null) {
                webSocketCallback.onMessage(text);
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            showLog("onClosed");
            if (webSocketCallback != null) {
                webSocketCallback.onClosed();
            }
            connected = false;
//            reconnect();
        }

        /**
         * Invoked when a web socket has been closed due to an error reading from or writing to the
         * network. Both outgoing and incoming messages may have been lost. No further calls to this
         * listener will be made.
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            showLog("onFailure " + t.getMessage());
            connected = false;
//            reconnect();
        }
    }

    private void connectSocket() {
        showLog("connect " + WS);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(WS).build();
        webSocket = client.newWebSocket(request, new WebSocketHandler());
        client.dispatcher().executorService().shutdown();
    }

    /**
     * 只暴露需要的回调给页面，onFailure 你给了页面，页面也无能为力不知怎么处理
     */
    public interface WebSocketCallback {
        void onMessage(String text);

        void onOpen();

        void onClosed();
    }

    private void showLog(String msg) {
        Log.e("webSocket", msg);
    }
}
