package com.lilanz.wificonnect.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 讯飞唤醒后的结果类
 */
public class WakeUpBean {
    private String sst;     // 操作类型
    private int id;         // 唤醒词id
    private int score;      // 得分
    private int bos;        // 前端点
    private int eos;        // 尾端点
    private String keyword; // 唤醒词，全拼

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
