package com.demon.tool.activity;

import android.app.Application;

import com.demon.tool.documentviewer.DocumentControl;
import com.demon.tool.thread.SaveDataThread;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DocumentControl.init(this);

        SaveDataThread.getInstance().start();
    }

}
