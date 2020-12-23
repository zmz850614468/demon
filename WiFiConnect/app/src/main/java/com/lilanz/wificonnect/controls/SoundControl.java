package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.media.SoundPool;

import com.lilanz.wificonnect.R;

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
        soundIdMap.put(R.raw.connect_me, 0);                 //
        soundIdMap.put(R.raw.pig_get_up, 0);                 //
        soundIdMap.put(R.raw.want_to_eat, 0);                //
        soundIdMap.put(R.raw.at_home, 0);                 //
        soundIdMap.put(R.raw.going_home, 0);                 //
        soundIdMap.put(R.raw.need_comfort, 0);                //
        soundIdMap.put(R.raw.pi_ka_qiu, 0);                //
        soundIdMap.put(R.raw.how_long, 0);                //
        soundIdMap.put(R.raw.ok_begin_timer, 0);                //

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        for (Map.Entry<Integer, Integer> entry : soundIdMap.entrySet()) {
            int soundId = soundPool.load(context, entry.getKey(), 0);
            entry.setValue(soundId);
        }
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
