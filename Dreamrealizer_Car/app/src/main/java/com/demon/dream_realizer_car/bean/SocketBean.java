package com.demon.dream_realizer_car.bean;

import com.demon.dream_realizer_car.util.StringUtil;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

/**
 * 用来与设备直接通信；不带任何头信息
 */
public class SocketBean implements ISendable {

    private String msg;

    public SocketBean() {
    }

    public SocketBean(String msg) {
        this.msg = msg;
    }

    @Override
    public byte[] parse() {
        if (StringUtil.isEmpty(msg)) {
            return "".getBytes();
        }
        return msg.getBytes();
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
