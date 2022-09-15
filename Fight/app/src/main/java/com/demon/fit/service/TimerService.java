package com.demon.fit.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.demon.fit.R;
import com.demon.fit.control.SoundControl;
import com.demon.fit.util.StringUtil;

public class TimerService extends Service {

    //    private Timer timer;
    private static TimerThread timerThread;

    @Override
    public void onCreate() {
        showLog("onCreate");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showLog("onStartCommand");

        if (timerThread != null) {
            stopCount();
        } else {
            startCount();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开始计数
     */
    private void startCount() {
        if (timerThread == null) {
            timerThread = new TimerThread();
            timerThread.start();
        }
    }

    private void stopCount() {
        if (timerThread != null) {
            timerThread.close();
            timerThread = null;
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        showLog("startService");
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        showLog("stopService");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        showLog("onDestroy");
//        stopCount();
        super.onDestroy();
    }

    private class TimerThread extends Thread {

        private boolean isContinue = true;
        long curTime = 0;
        boolean isTip = false;
        int house;

        @Override
        public void run() {

            while (isContinue) {
                house = Integer.parseInt(StringUtil.getHouse());
//                if (house >= 21) {  // 只有晚上才做提示
                    curTime = System.currentTimeMillis();
                    curTime /= 1000;
                    if (curTime % 300 >= 240) {
                        if (!isTip) {
                            isTip = true;
                            showLog("还剩60秒钟");
                            SoundControl.getInstance(getBaseContext()).play(R.raw.succeed, 2);
                        }
                    } else {
                        isTip = false;
                    }
//                }

//                showLog("运行：" + house + " - " + curTime % 300);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        public void close() {
            isContinue = false;
        }

    }

    ;

    private void showLog(String msg) {
        Log.e("TimerService", msg);
    }
}
