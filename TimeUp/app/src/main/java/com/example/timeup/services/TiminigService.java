package com.example.timeup.services;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.example.timeup.R;

import java.util.Timer;
import java.util.TimerTask;

public class TiminigService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int count = 0;

            @Override
            public void run() {
                count++;
                showMsg(count + "");
            }
        }, 1000, 1000);
        binderNotification();
    }


    @Nullable
    @Override
    public TimingBinder onBind(Intent intent) {
        return null;
    }


    public class TimingBinder extends Binder {
    }

    private void binderNotification() {
    }

    @Override
    public void onDestroy() {
        showMsg("service onDestroy");
        super.onDestroy();
    }

    private void showMsg(String msg) {
        Log.e("service", msg);
    }
}
