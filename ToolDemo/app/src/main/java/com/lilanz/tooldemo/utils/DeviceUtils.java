package com.lilanz.tooldemo.utils;

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
