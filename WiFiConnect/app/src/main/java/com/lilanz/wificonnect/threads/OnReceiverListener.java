package com.lilanz.wificonnect.threads;

import com.lilanz.wificonnect.SocketThread;

public abstract class OnReceiverListener {

    public abstract void onReceiver(String msg);

    public void onReceiver(SocketThread thread, String msg) {
    }

}
