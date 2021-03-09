package com.lilanz.wificonnect.bean_new;

import com.lilanz.wificonnect.utils.StringUtil;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

import java.util.Collections;

/**
 * 正规的OkSocket通信，带四个byte的长度信息
 */
public class OkSocketBean implements ISendable {

    private String msg;

    public OkSocketBean() {
    }

    public OkSocketBean(String msg) {
        this.msg = msg;
    }

    @Override
    public byte[] parse() {
        if (StringUtil.isEmpty(msg)) {
            msg = "";
        }
        byte[] msgByte = msg.getBytes();
        int length = msgByte.length;
        byte[] newByte = new byte[length + 4];

        for (int i = 3; i >= 0; i--) {
            newByte[i] = (byte) (length % 256);
            length = length / 256;
        }

        for (int i = 4; i < length + 4; i++) {
            newByte[i] = msgByte[i - 4];
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
