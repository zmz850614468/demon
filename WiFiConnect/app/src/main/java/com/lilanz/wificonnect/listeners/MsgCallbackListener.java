package com.lilanz.wificonnect.listeners;

import com.lilanz.wificonnect.beans.SongBean;

import java.util.List;

/**
 * 网络消息回调监听
 */
public abstract class MsgCallbackListener {

    /**
     * 歌曲信息更新回调
     *
     * @param bean
     */
    public void onMusicUpdate(SongBean bean) {
    }

    /**
     * 服务器播放下一首歌曲，通知
     */
    public void onPlayNextMusic() {
    }
}
