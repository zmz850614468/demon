package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.media.AudioManager;

import com.google.gson.Gson;
import com.lilanz.wificonnect.beans.VoiceBean;

/**
 * 应用数据控制类
 */
public class AppDataControl {

    /**
     * 音乐是否在播放
     */
    public static boolean isPlaying = false;

    /**
     * 正在播放的音乐路径
     */
    public static String playingPath = "";

    /**
     * 音乐的最多声音
     */
    public static int maxMusicVoice = 0;

    /**
     * 音乐的当前音量
     */
    public static int curMusicVoice = 0;

    /**
     * 音乐播放模式
     */
    public static int playMode = MediaControl.PLAY_LIST;

    public static void parseVoiceInfo(String msg) {
        try {
            VoiceBean bean = new Gson().fromJson(msg, VoiceBean.class);
            maxMusicVoice = bean.maxVoice;
            curMusicVoice = bean.currentVoice;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
