package com.demon.agv.oksocket;

/**
 * OkSock 监听对象
 */
public abstract class OnSocketListener {

    abstract void onMsgCallback(String ip, String msg);

    void onDisconnect(String ip) {
    }

}