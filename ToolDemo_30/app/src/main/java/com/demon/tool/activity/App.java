package com.demon.tool.activity;

import android.app.Application;

import com.demon.tool.documentviewer.DocumentHelp;
import com.demon.tool.thread.SaveDataThread;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new DocumentHelp().init(this);

        SaveDataThread.getInstance().start();
    }

}
