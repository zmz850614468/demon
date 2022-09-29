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

import java.util.ArrayList;

public class TimerService extends Service {

    //    private Timer timer;
    private static TimerThread timerThread;
    private ArrayList<String> tipList;  // 整点提醒时间

    @Override
    public void onCreate() {
        showLog("onCreate");
        super.onCreate();

        initData();
    }

    private void initData() {
        tipList = new ArrayList<>();
        tipList.add("09:58");
        tipList.add("11:13");
        tipList.add("14:13");
        tipList.add("14:58");
        tipList.add("21:58");
        tipList.add("22:58");
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
        private String dayTime;

        @Override
        public void run() {

            while (isContinue) {
                curTime = System.currentTimeMillis();
                curTime /= 1000;
                dayTime = StringUtil.getDayTime();
                if (tipList.contains(dayTime)) {
                    if (!isTip) {
                        isTip = true;
                        showLog("整点提示");
                        SoundControl.getInstance(getBaseContext()).play(R.raw.succeed, 4, 1.2f);
                    }
                } else if ("10:18".equals(dayTime) || "10:23".equals(dayTime)) { // 休息时间，不做提示
                    if (!isTip) {
                        isTip = true;
                    }
                } else if (curTime % 300 >= 240) {  // 其他5分K线结束前一分钟提示
                    if (!isTip) {
                        isTip = true;
                        showLog("还剩60秒钟");
                        SoundControl.getInstance(getBaseContext()).play(R.raw.succeed, 2);
                    }
                } else {
                    isTip = false;
                }

//                curTime = System.currentTimeMillis();
//                curTime /= 1000;
//                if (curTime % 300 >= 240) {
//                    if (!isTip) {
//                        isTip = true;
//                        showLog("还剩60秒钟");
//                        SoundControl.getInstance(getBaseContext()).play(R.raw.succeed, 1);
//                    }
//                } else {
//                    isTip = false;
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


    private void showLog(String msg) {
        Log.e("TimerService", msg);
    }
}
