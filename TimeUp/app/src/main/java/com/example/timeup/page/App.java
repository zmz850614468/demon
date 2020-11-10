package com.example.timeup.page;

import android.app.Application;
import android.content.Context;

import com.example.timeup.controls.MediaControl;
import com.example.timeup.controls.TypeDBControl;
import com.tencent.bugly.Bugly;

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        MediaControl.getInstance(this).initData();

        Bugly.init(this, "a999667c27", false);

        TypeDBControl.updateDBFromFile(context);
    }
}
