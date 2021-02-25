package com.lilanz.wificonnect.beans;


import androidx.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 音量对象
 */
public class VoiceBean {

    public int maxVoice;

    public int currentVoice;

    public VoiceBean() {
    }

    public VoiceBean(int maxVoice, int currentVoice) {
        this.maxVoice = maxVoice;
        this.currentVoice = currentVoice;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
