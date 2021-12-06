package com.demon.remotecontrol.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

/**
 * 设备相关帮助类
 */
public class DeviceUtils {

    /**
     * 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取设备序列号
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getSerialNumber(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "null";
            }
            return android.os.Build.getSerial();
        }
        return android.os.Build.SERIAL;
    }

    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机名称
     **/
    public static String getPhoneName() {
        return android.os.Build.MANUFACTURER;
    }

}
