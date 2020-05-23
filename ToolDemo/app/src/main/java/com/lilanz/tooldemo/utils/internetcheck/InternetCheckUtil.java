package com.lilanz.tooldemo.utils.internetcheck;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class InternetCheckUtil {

    private static NetPingManager netPingManager;

    public static void internetCheck(final Context context, String url) {
        final Handler canHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (netPingManager != null) {
                            Toast.makeText(context, "网络延迟：" + msg.obj + "ms", Toast.LENGTH_LONG).show();
                            netPingManager.release();
                            netPingManager = null;
                        }
                        break;
                    case 2:
                        if (netPingManager != null) {
                            netPingManager.release();
                            netPingManager = null;
                            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        };

        netPingManager = new NetPingManager(context, url, new NetPingManager.IOnNetPingListener() {
            int count = 0;
            int total = 0;

            @Override
            public void ontDelay(long log) {
                count++;
                total += log;
                if (count == 3) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = total / count;
                    canHandle.sendMessage(msg);
                }
            }

            @Override
            public void onError() {
                Message msg = new Message();
                msg.what = 2;
                canHandle.sendMessage(msg);
            }
        });
        netPingManager.startGetDelay();
    }


}
