package com.demon.tool.data_transfer.websocket;

/**
 * 传输的消息类型 类
 */
public class MsgTypeBean {

    public MsgTypeBean() {
    }

    public MsgTypeBean(String type, String msg) {
        this.msgType = type;
        this.msg = msg;
    }

    public String msgType;

    public String msg;
}
