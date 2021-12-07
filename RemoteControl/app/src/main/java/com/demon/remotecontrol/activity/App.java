package com.demon.remotecontrol.activity;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.demon.remotecontrol.util.DeviceUtils;
import com.tencent.bugly.Bugly;

public class App extends Application {

    public static String deviceId;
    public static String selectedDevice;
//        public static String host = "http://192.168.37.43:15014";
    public static String host = "http://webt.lilang.com:9001";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

//        deviceId = DeviceUtils.getSerialNumber(this);
        deviceId = DeviceUtils.getAndroidId(this).toUpperCase();

        Bugly.init(this, "7bd05b63d8", false);
    }
}
