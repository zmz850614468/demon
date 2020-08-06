package com.lilanz.tooldemo.multiplex.wificonnect.threads;

import io.socket.client.Socket;

public class ServerThread extends Thread {

    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
    }
}
