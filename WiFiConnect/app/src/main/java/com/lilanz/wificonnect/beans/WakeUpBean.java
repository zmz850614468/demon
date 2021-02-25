package com.lilanz.wificonnect.beans;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 讯飞唤醒后的结果类
 */
public class WakeUpBean {
    public String sst;     // 操作类型
    /**
     * 0:小恶魔开
     * 1:小恶魔关
     * 2:小恶魔打
     * 3:小恶魔播
     */
    public int id;         // 唤醒词id
    
    public int score;      // 得分
    public int bos;        // 前端点
    public int eos;        // 尾端点
    public String keyword; // 唤醒词，全拼

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
