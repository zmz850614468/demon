package com.demon.fit.activity;

import android.app.Application;
import android.content.Context;

import com.demon.fit.util.VibratorUtil;

public class App extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static void vibrator(){
        VibratorUtil.vibrator(context);
    }
}
