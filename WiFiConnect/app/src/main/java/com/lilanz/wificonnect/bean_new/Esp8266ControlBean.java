package com.lilanz.wificonnect.bean_new;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 设备控制类
 */
public class Esp8266ControlBean {

    public String ip;

    public int port;

    /**
     * open:
     * close:
     * query:
     */
    public String control;      // 控制方式

    public Esp8266ControlBean() {
    }

    public Esp8266ControlBean(String ip, int port, String control) {
        this.ip = ip;
        this.port = port;
        this.control = control;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
