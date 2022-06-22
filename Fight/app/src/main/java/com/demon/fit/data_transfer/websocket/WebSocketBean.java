package com.demon.fit.data_transfer.websocket;

import com.google.gson.Gson;

public class WebSocketBean {

    public WebSocketBean() {
    }

    public WebSocketBean(String ip, int port, String msgType, String msg) {
        this.ip = ip;
        this.port = port;
        this.msgType = msgType;
        this.msg = msg;
    }

    public String ip;
    public int port;
    public String msgType;
    public String msg;

    public String getData() {
        return new Gson().toJson(new MsgTypeBean(msgType, msg));
    }
}
