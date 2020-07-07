package com.lilanz.kotlintool

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Logger日志的初始化
        Logger.addLogAdapter(AndroidLogAdapter());

    }
}