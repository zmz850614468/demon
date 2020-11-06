package com.example.timeup.page;

import android.app.Application;
import android.content.Context;

import com.example.timeup.controls.MediaControl;

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        MediaControl.getInstance(this).initData();
    }
}
