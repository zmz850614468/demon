package com.demon.module.base;

import android.app.Application;

import com.demon.module.di.component.AppComponent;
import com.demon.module.di.component.DaggerAppComponent;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 应用内单例，注入方式
     *
     * @return
     */
    public static synchronized AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.create();
        }

        return appComponent;
    }
}
