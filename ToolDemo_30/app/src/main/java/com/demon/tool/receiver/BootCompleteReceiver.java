package com.demon.tool.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.demon.tool.activity.MainActivity;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent thisIntent = new Intent(context, MainActivity.class);
                    thisIntent.setAction("android.intent.action.MAIN");
                    thisIntent.addCategory("android.intent.category.LAUNCHER");
                    thisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(thisIntent);
                }
            }, 5000);
        }
    }

}