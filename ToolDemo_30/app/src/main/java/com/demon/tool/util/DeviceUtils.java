package com.demon.tool.util;

import android.content.Context;
import android.provider.Settings;

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
    public static String getSerialNumber() {
        return android.os.Build.SERIAL;
    }

    /**
     * 获取安卓 id号
     *
     * @param context
     * @return
     */
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
