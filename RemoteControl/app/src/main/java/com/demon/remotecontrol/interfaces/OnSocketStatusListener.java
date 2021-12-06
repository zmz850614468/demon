package com.demon.remotecontrol.interfaces;

public interface OnSocketStatusListener {

    void onConnected();

    void onDisconnected();

    void onError(String errMsg);
}
