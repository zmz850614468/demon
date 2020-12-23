package com.lilanz.wificonnect.test;

import com.lilanz.wificonnect.utils.StringUtil;
import com.xuhao.didi.core.iocore.interfaces.ISendable;

public class TestBean implements ISendable {

    private String msg;

    public TestBean() {
    }

    public TestBean(String msg) {
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
}
