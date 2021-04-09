package com.demon.agv.oksocket;

/**
 * OkSock 监听对象
 */
public abstract class OnSocketListener {

    protected abstract void onMsgCallback(String ip, String msg);

    protected void onDisconnected(String ip) {
    }

    protected void onConnected(String ip) {
    }

}