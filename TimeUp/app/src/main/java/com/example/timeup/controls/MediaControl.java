package com.example.timeup.controls;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.timeup.R;

import java.util.ArrayList;
import java.util.List;

public class MediaControl {

    private Context context;
    private static MediaControl mediaControl;
    private List<Integer> musicIdList;

    private MediaPlayer player;

    private MediaControl(Context context) {
        this.context = context;
    }

    public static MediaControl getInstance(Context context) {
        if (mediaControl == null) {
            mediaControl = new MediaControl(context);
        }
        return mediaControl;
    }

    /**
     * 初始化声音数据
     */
    public void initData() {
        musicIdList = new ArrayList<>();
        musicIdList.add(R.raw.qiaobgn);                 // 已添加过该商品
        musicIdList.add(R.raw.xiaob);                 // 已添加过该商品
        musicIdList.add(R.raw.yuanfyxy);                 // 已添加过该商品
    }


    public void stopPlay() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public void play(int rawId) {
        stopPlay();
        player = MediaPlayer.create(context, rawId);
        player.start();
    }

    private int index = 0;

    public void playRandon() {
        index++;
        index = index % musicIdList.size();
        int rawId = musicIdList.get(index);

        stopPlay();
        player = MediaPlayer.create(context, rawId);
        player.start();
    }

}
