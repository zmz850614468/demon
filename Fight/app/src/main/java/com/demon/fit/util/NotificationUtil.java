package com.demon.fit.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.demon.fit.R;

public class NotificationUtil {

//    public static void sendNotification(Context context) {
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(context).setDefaults(Notification.DEFAULT_ALL);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setPriority(Notification.PRIORITY_DEFAULT);
//        builder.setContentTitle("title");
//        long[] patter = {100, 800, 300, 800};
//        builder.setVibrate(patter);
//        builder.setContentText("振动通知");
//        manager.notify(1, builder.build());
//    }

    private static int id = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void sendNotification(Context context, String msg) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel("ShadyPi", "ShadyPi",
                NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(context, "ShadyPi")
                .setContentTitle(msg)
//                .setContentText("This is content text")
                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_HIGH) //设置优先级
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .build();


        manager.notify(id++, notification);

    }

}
