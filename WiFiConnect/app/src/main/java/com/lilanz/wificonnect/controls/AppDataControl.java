package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.VoiceBean;
import com.lilanz.wificonnect.threads.WifiService;

import java.util.List;

/**
 * 应用数据控制类
 */
public class AppDataControl {

    /**
     * 全局WiFi服务
     */
    public static WifiService wifiService;

    /**
     * 音乐是否在播放
     */
    public static boolean isPlaying = false;

    /**
     * 正在播放的音乐路径
     */
    public static String playingPath = "";

    /**
     * 音乐的最大声音
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

    /**
     * 解析音乐的声音信息
     *
     * @param msg
     */
    public static void parseVoiceInfo(@NonNull String msg) {
        try {
            VoiceBean bean = new Gson().fromJson(msg, VoiceBean.class);
            maxMusicVoice = bean.maxVoice;
            curMusicVoice = bean.currentVoice;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 让wifi服务发送信息
     *
     * @param bean
     */
    public static void sendMsg(@NonNull MsgBean bean) {
        if (wifiService != null) {
            wifiService.sendMsg(bean.toString());
        }
    }

    public static void sendMsg(@NonNull List<MsgBean> list) {
        if (wifiService != null) {
            wifiService.sendMsg(list);
        }
    }

}
