package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.google.gson.Gson;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.beans.SongBean;
import com.lilanz.wificonnect.beans.VoiceBean;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.StringUtil;

import java.io.IOException;
import java.util.List;

public class MediaControl {

    public static final int PLAY_LIST = 1;      // 列表播放
    public static final int PLAY_ONLY_ONE = 2;  // 单曲播放
    public static final int PLAY_RANDOM = 3;    // 随机播放

    private Context context;
    private static MediaControl mediaControl;
    private MediaPlayer player;

    private WifiService wifiService;

    private List<SongBean> songList;
    private int playIndex;  // 正在播放的音乐下标

    private MediaControl(Context context) {
        this.context = context;
        songList = SongControl.getSongList(context);
    }

    public static MediaControl getInstance(Context context) {
        if (mediaControl == null) {
            mediaControl = new MediaControl(context);
        }
        return mediaControl;
    }

    public void stopPlay() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
    }

    /**
     * 设置歌曲播放结束后 监听
     *
     * @param player
     */
    private void setListener(MediaPlayer player) {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNext();
            }
        });
    }

    /**
     * 播放指点的音乐
     *
     * @param path
     */
    public void play(String path) {
//        stopPlay();
        seekMusicIndex(path);
        if (player != null) {
            player.reset();
            try {
                player.setDataSource(path);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player = MediaPlayer.create(context, Uri.parse(path));
            setListener(player);
        }
        player.start();
    }

    /**
     * 根据音乐列表查找音乐 下标
     *
     * @param path
     */
    private void seekMusicIndex(String path) {
        if (!StringUtil.isEmpty(path)) {
            int size = songList.size();
            for (int i = 0; i < size; i++) {
                if (path.equals(songList.get(i).path)) {
                    playIndex = i;
                    break;
                }
            }
        }
    }

    /**
     * 播放下一曲音乐
     */
    public void playNext() {
        // TODO 音乐播放方式
        switch (AppDataControl.playMode) {
            case MediaControl.PLAY_LIST:    // 顺序播放
                playIndex++;
                if (playIndex >= songList.size()) {
                    playIndex = 0;
                }
                break;
            case MediaControl.PLAY_RANDOM:  // 随机播放
                playIndex = ((int) (Math.random() * 10000)) % songList.size();
                break;
            case MediaControl.PLAY_ONLY_ONE:    // 单曲播放
                break;
        }

        if (!songList.isEmpty()) {
            play(songList.get(playIndex).path);
            sendPlayingInfo();
        }
    }

    /**
     * 获取音乐音量大小
     */
    public String getVoiceInfo() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVoice = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVoice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        return new VoiceBean(maxVoice, currentVoice).toString();
    }

    /**
     * 设置音乐 播放音量
     *
     * @param currentVoice
     */
    public void setPlayingVoice(int currentVoice) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, AudioManager.FLAG_PLAY_SOUND);
    }

    /**
     * 发送现在正在播放的歌曲信息
     */
    public void sendPlayingInfo() {
        if (wifiService != null && playIndex < songList.size()) {
            MsgBean bean = getPlayingInfo();
            if (bean != null) {
                wifiService.sendMsg(bean.toString());
            }
        }
    }

    /**
     * 0:path: 暂停：歌曲地址
     * 1:path: 播放：歌曲地址
     *
     * @return
     */
    public MsgBean getPlayingInfo() {
        if (playIndex < songList.size()) {
            String content = isPlaying() ? "1" : "0";
            content += "-" + songList.get(playIndex).path;
            return new MsgBean(MsgBean.MUSIC_PAUSE_OR_START, content);
        }
        return null;
    }

    /**
     * 切换播放状态
     */
    public void switchPlay(String path) {
        if (isPlaying()) {
            player.pause();
        } else {
            if (player == null) {
                play(path);
            } else {
                player.start();
            }
        }
    }

    /**
     * 是否正在播放音乐
     *
     * @return
     */
    public boolean isPlaying() {
        if (player != null) {
            return player.isPlaying();
        }
        return false;
    }

    public void setSongList(List<SongBean> songList) {
        this.songList.clear();
        this.songList.addAll(songList);
    }

    public void setWifiService(WifiService wifiService) {
        this.wifiService = wifiService;
    }
}
