package com.lilanz.wificonnect.activitys;

import android.app.Application;
import android.content.Context;

import com.lilanz.wificonnect.controls.SoundControl;
import com.lilanz.wificonnect.utils.BuildUtil;
import com.tencent.bugly.Bugly;

public class App extends Application {

    public static Context context;
    public static boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        isDebug = BuildUtil.isApkInDebug(this);

        Bugly.init(context, "1fa246be97", false);

        SoundControl.getInstance(context).initData();

    }
}
