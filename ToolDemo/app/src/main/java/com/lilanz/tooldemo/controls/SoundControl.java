package com.lilanz.tooldemo.controls;

import android.content.Context;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 声音控制类：单例模式
 */
public class SoundControl {

    private Context context;
    private static SoundControl soundControl;
    private Map<Integer, Integer> soundIdMap;

    private SoundPool soundPool;

    private SoundControl(Context context) {
        this.context = context;
    }

    public static SoundControl getInstance(Context context) {
        if (soundControl == null) {
            soundControl = new SoundControl(context);
        }
        return soundControl;
    }

    /**
     * 初始化声音数据
     */
    public void initData() {
        soundIdMap = new HashMap<>();
//        soundIdMap.put(R.raw.had_added, 0);                 // 已添加过该商品

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        for (Map.Entry<Integer, Integer> entry : soundIdMap.entrySet()) {
            int soundId = soundPool.load(context, entry.getKey(), 0);
            entry.setValue(soundId);
        }
//        fuKUploadId = soundPool.load(this, R.raw.fuk_confirm, 0);
    }

    /**
     * 播放声音
     *
     * @param rawId
     */
    public void play(int rawId) {
        if (soundIdMap.containsKey(new Integer(rawId))) {
            int soundId = soundIdMap.get(new Integer(rawId));
            soundPool.play(soundId, 1, 1, 0, 0, 1);
        }
    }

    /**
     * 播放声音
     *
     * @param rawId
     */
    public void play(int rawId, int times) {
        if (soundIdMap.containsKey(new Integer(rawId))) {
            int soundId = soundIdMap.get(new Integer(rawId));
            soundPool.play(soundId, 1, 1, 0, times, 1);
        }
    }

    /**
     * 停止音效播放
     *
     * @param rawId
     */
    public void stop(int rawId) {
        if (soundIdMap.containsKey(new Integer(rawId))) {
            int soundId = soundIdMap.get(new Integer(rawId));
            soundPool.stop(soundId);
        }
    }

}
