package com.lilanz.wificonnect.controls;

import android.util.Log;

import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.beans.StopBean;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 音乐时间控制器
 */
public class MusicTimerControl {

    public static int stopMode = MediaControl.STOP_NONE;
    public static int songCount = 1;
    private static int timeCount = 1;

    private Timer timer;

    public void updateStopMode(StopBean bean) {
        stopMode = bean.stopMode;

        switch (bean.stopMode) {
            case MediaControl.STOP_NONE:
                clear();
                break;
            case MediaControl.STOP_TIME:
                clear();
                timeCount = bean.count * 60;
                startTimer();
                break;
            case MediaControl.STOP_SONG_COUNT:
                clear();
                songCount = bean.count;
                break;
        }
    }

    private void clear() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        stopMode = MediaControl.STOP_NONE;
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeCount--;
//                showLog("timerCount:" + timeCount);
                if (timeCount <= 0) {
                    timer.cancel();
                    timer = null;
                    if (onMusicStopListener != null) {
                        onMusicStopListener.onMusicStop();
                    }
                }
            }
        }, 1000, 1000);

    }

    public static void subSongCount() {
        songCount--;
    }

    private OnMusicStopListener onMusicStopListener;

    public void setOnMusicStopListener(OnMusicStopListener onMusicStopListener) {
        this.onMusicStopListener = onMusicStopListener;
    }

    public interface OnMusicStopListener {
        void onMusicStop();
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("musicTimer", msg);
        }
    }

}
