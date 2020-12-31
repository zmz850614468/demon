package com.lilanz.wificonnect.bean_new;

import android.support.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
