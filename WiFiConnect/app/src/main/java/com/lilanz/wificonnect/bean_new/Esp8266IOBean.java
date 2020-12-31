package com.lilanz.wificonnect.bean_new;

import com.lilanz.wificonnect.utils.StringUtil;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

/**
 * 用来与设备直接通信；不带任何头信息
 */
public class Esp8266IOBean implements ISendable {

    private String msg;

    public Esp8266IOBean() {
    }

    public Esp8266IOBean(String msg) {
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
