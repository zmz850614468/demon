package com.lilanz.wificonnect.beans;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 设备控制类
 */
public class DeviceControlBean {

    public String ip;

    public int port;

//    public int type;            // 类型

    /**
     * open:
     * close:
     * status:
     */
    public String control;      // 控制方式

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
