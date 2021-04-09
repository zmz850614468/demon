package com.demon.agv.activity;

import android.app.Application;

import com.demon.agv.debug.CrashHandler;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 打印本地日志
        CrashHandler.getInstance().init(this);
    }
}
