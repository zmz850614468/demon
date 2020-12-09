package com.lilanz.wificonnect.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lilanz.wificonnect.utils.StringUtil;

/**
 * 消息对象
 */
public class MsgBean {

    public static final int IDENTITY = 1;           // 身份信息
    public static final int COMMUNICATE = 2;        // 聊天信息或测试信息
    public static final int PLAY_VOICE = 3;        // 播放音效

    public static final int PLAY_MUSIC = 4;             // 播放音乐
    public static final int UPDATE_MUSIC = 40;          // 更新音乐列表
    public static final int UPDATE_MUSIC_RESULT = 41;   // 返回更新的音乐列表
    public static final int MUSIC_PAUSE_OR_START = 42;  // 服务端：暂停或播放音乐 ； 客户端：通知目前正在播放的信息
    public static final int MUSIC_GET_VOICE_INFO = 43;  // 获取音量信息
    public static final int MUSIC_VOICE_INFO = 44;      // 音乐的播放音量
    public static final int MUSIC_PLAY_MODE = 45;       // 歌曲播放模式
    public static final int MUSIC_STOP_MODE = 46;       // 歌曲停止模式

    public static final int FILE_EXCHANGE = 5;          // 文件传输

    public static final int DEVICE_CONTROL = 6;         // 设备控制

    public static final int RECEIVE_TIP = 10;           // 服务器收到消息，返回提示

    public int type;            // 消息类型

    public String content;      // 消息内容

    public int orderId;         // 文件序号

    public boolean isLast;      // 是否是最后一个文件

    public byte[] bytes;        // 字节文件

    public MsgBean() {
    }

    public MsgBean(int type, String content) {
        this.type = type;
        this.content = content;
    }


    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
