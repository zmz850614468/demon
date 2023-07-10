package com.demo.fightdata.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.demo.fightdata.R;

public class NotificationUtil {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("ShadyPi", "ShadyPi",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(context, "ShadyPi")
//                .setContentTitle("This is content title")
//                .setContentText("This is content text")
                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH) //设置优先级
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();


        manager.notify(1, notification);
    }
}
