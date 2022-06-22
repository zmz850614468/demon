package com.demon.fit.data_transfer.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WSocketServerThread extends Thread {

    private static final int SLEEP_TIME = 60000;

    private WSocketServer wSocketServer;
    private int port;
    private WSocketServer.OnWebSocketServerListener listener;

    private boolean isDestroy;
    public List<WebSocketBean> list = new ArrayList<>();

    public WSocketServerThread(int port, WSocketServer.OnWebSocketServerListener listener) {
        this.port = port;
        this.listener = listener;
        isDestroy = false;
        startWebSocketServer();
    }

    public synchronized void addMsg(String ip, int port, String msgType, String msg) {
        list.add(new WebSocketBean(ip, port, msgType, msg));
        if (list.size() == 1) {
            interrupt();
        }
    }

    public synchronized WebSocketBean removeMsg() {
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public void run() {
        super.run();

        WebSocketBean bean = null;
        while (!isDestroy) {

            if (bean == null) {
                bean = removeMsg();
            }

            if (bean != null) {
                if (wSocketServer != null && wSocketServer.isOpened()) {
                    wSocketServer.send(bean.ip, bean.port, bean.getData());
                    bean = null;
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

    /**
     * 开启 webSocketServer服务
     */
    public void startWebSocketServer() {
        if (wSocketServer != null) {
            try {
                wSocketServer.stop();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
            wSocketServer = null;
        }

        wSocketServer = new WSocketServer(port);
        wSocketServer.setOnWebSocketServerListener(listener);
        wSocketServer.start();
    }

    public void onDestroy() {
        if (wSocketServer != null) {
            try {
                wSocketServer.stop();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isDestroy = true;
    }
}
