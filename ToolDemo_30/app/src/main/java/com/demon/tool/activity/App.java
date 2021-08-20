package com.demon.tool.activity;

import android.app.Application;

import com.demon.tool.documentviewer.DocumentHelp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new DocumentHelp().init(this);
    }
}
