package com.lilanz.tooldemo;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class App extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        // Logger日志的初始化
        Logger.addLogAdapter(new AndroidLogAdapter());

        // 网络调试，数据库查看框架
//        Stetho.initializeWithDefaults(this);
    }
}
