package com.example.timeup.page;

import android.app.Application;
import android.content.Context;

import com.example.timeup.controls.MediaControl;
import com.example.timeup.controls.TypeDBControl;
import com.example.timeup.utils.BuildUtil;
import com.tencent.bugly.Bugly;

public class App extends Application {

    public static Context context;
    public static boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        isDebug = BuildUtil.isApkInDebug(context);
        MediaControl.getInstance(this).initData();

        Bugly.init(context, "a999667c27", false);

        TypeDBControl.updateDBFromFile(context);
    }
}
