package com.lilanz.wificonnect.controls;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.VoiceBean;
import com.lilanz.wificonnect.listeners.MsgCallbackListener;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;

import java.util.List;

/**
 * 应用数据控制类
 */
public class AppDataControl {

    /**
     * 欢迎界面选择进入的类型（3种类型）
     * 服务器
     * 客户端
     * 直接控制设备
     */
    public static String selectedType;

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

    /**
     * 添加监听
     *
     * @param listener
     */
    public static void addCallbackListener(MsgCallbackListener listener) {
        if (wifiService != null) {
            wifiService.addMsgCallBackListener(listener);
        }
    }

    /**
     * 移除监听
     *
     * @param listener
     */
    public static void removeCallbackListener(MsgCallbackListener listener) {
        if (wifiService != null) {
            wifiService.removeCallbackListener(listener);
        }
    }

    /**
     * 重连wifiService 服务
     */
    public static void reconnectWifiService(Context context) {
        if (wifiService != null) {
            wifiService.close();

            String selectedIpType = SharePreferencesUtil.getSelectedIpType(context);
            String ip = SharePreferencesUtil.getServiceIp(context);        // 默认广域网服务器地址
            if (selectedIpType.equals("局域网")) {
                ip = SharePreferencesUtil.getInsideServiceIp(context);     // 局域网服务器地址
            }
            int port = SharePreferencesUtil.getServicePort(context);
            wifiService.startConnect(ip, port);
        }
    }

}
