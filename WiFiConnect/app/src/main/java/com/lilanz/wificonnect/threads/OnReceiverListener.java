package com.lilanz.wificonnect.threads;

public abstract class OnReceiverListener {

    /**
     * 发生错误时
     *
     * @param msg
     */
    public void onError(SocketThread thread, String msg) {
    }

    /**
     * 本地端的提示信息
     *
     * @param type 0：提示信息 1:连接成功 2：断开连接
     * @param msg
     */
    public abstract void onTip(int type, String msg);

    /**
     * 客服端收到的信息
     *
     * @param msg
     */
    public void onReceiver(String msg) {
    }

    /**
     * 服务端收的信息
     *
     * @param thread
     * @param msg
     */
    public void onReceiver(SocketThread thread, String msg) {
    }

}
