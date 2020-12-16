package com.lilanz.wificonnect.listeners;

import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
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

    /**
     * 设备更新成功
     */
    public void onDeviceUpdateCallback(String msg) {
    }

    /**
     * 控制设备的回调
     *
     * @param bean
     */
    public void onControlCallback(DeviceControlBean bean) {
    }

    /**
     * 拉取所有设备信息
     *
     * @param bean
     */
    public void onDeviceRefresh(DeviceBean bean, boolean isLast) {
    }

    /**
     * 获取设备状态信息
     *
     * @param bean
     */
    public void onDeviceStatusUpdate(DeviceBean bean) {
    }
}
