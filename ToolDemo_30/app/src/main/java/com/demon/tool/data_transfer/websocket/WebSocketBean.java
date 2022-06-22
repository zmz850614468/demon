package com.demon.tool.data_transfer.websocket;

public class WebSocketBean {

    public WebSocketBean() {
    }

    public WebSocketBean(String ip, int port, String msg) {
        this.ip = ip;
        this.port = port;
        this.msg = msg;
    }

    public String ip;
    public int port;
    public String msg;
}
