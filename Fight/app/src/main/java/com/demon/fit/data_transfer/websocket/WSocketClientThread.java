package com.demon.fit.data_transfer.websocket;

import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WSocketClientThread extends Thread {

    private static final int SLEEP_TIME = 60000;

    private WSocketClient wSocketClient;
    private WSocketClient.OnWebSocketClientListener onWebSocketClientListener;
    private boolean isDestroy;
    private String uri;

    private List<String> webSocketList = new ArrayList<>();

    public WSocketClientThread(String uri, WSocketClient.OnWebSocketClientListener listener) {
        this.onWebSocketClientListener = listener;
        this.uri = uri;
        isDestroy = false;
        connectWebSocket();
    }

    public synchronized void addData(String msgType, String msg) {
        webSocketList.add(new Gson().toJson(new MsgTypeBean(msgType, msg)));
        if (webSocketList.size() == 1) {
            interrupt();
        }
    }

    public synchronized void addData(List<String> list) {
        webSocketList.addAll(list);
        if (webSocketList.size() == list.size()) {
            interrupt();
        }
    }

    private synchronized String removeData() {
        if (!webSocketList.isEmpty()) {
            return webSocketList.remove(0);
        }
        return null;
    }

    @Override
    public void run() {
        super.run();

        String data = null;
        while (!isDestroy) {

            if (data == null) {
                data = removeData();
            }

            if (data != null) {
                if (wSocketClient != null && wSocketClient.isOpen()) {
                    wSocketClient.send(data);
                    data = null;
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // todo 可以睡眠一段时间
                continue;
            }

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void connectWebSocket() {
        wSocketClient = new WSocketClient(URI.create(uri));
        wSocketClient.setOnWebSocketClientListener(onWebSocketClientListener);
        wSocketClient.connect();
    }

    public void onDestroy() {
        if (wSocketClient != null && wSocketClient.isOpen()) {
            wSocketClient.onDestroy();
            wSocketClient = null;
        }
        isDestroy = true;
        interrupt();
    }
}
