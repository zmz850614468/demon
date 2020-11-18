package com.demon.myapplication.activitys;

import android.app.Application;
import android.content.Context;

import com.demon.myapplication.controls.DBControl;
import com.tencent.bugly.Bugly;

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Bugly.init(this, "5585fac602", false);

        DBControl.updateDBFromFile(this);
    }
}
