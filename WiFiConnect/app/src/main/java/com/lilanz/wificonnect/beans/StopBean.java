package com.lilanz.wificonnect.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 音乐播放结束类
 */
public class StopBean {

    public int stopMode;    // 停止播放模式
    public int count;       // 数值

    public StopBean() {
    }

    public StopBean(int stopMode, int count) {
        this.stopMode = stopMode;
        this.count = count;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
