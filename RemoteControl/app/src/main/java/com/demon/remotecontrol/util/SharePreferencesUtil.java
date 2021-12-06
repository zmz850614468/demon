package com.demon.remotecontrol.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";

    private static SharedPreferences instance = null;

    private static final String DEVICE_NO = "deviceNo";   //
    private static final String DEVICE_TYPE = "deviceType";   //

    // 获取
    public static String getDeviceType(Context context) {
        return getInstance(context).getString(DEVICE_TYPE, "");
    }

    public static void saveDeviceType(Context context, String deviceType) {
        setString(context, DEVICE_TYPE, deviceType);
    }

    // 获取
    public static String getDeviceNo(Context context) {
        return getInstance(context).getString(DEVICE_NO, "");
    }

    public static void saveDeviceNo(Context context, String deviceNo) {
        setString(context, DEVICE_NO, deviceNo);
    }

    //============================      以下为固定模式        =============================

    // 保存int型数据
    private static void setInteger(Context context, String key, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // 保存float型数据
    private static void setFloat(Context context, String key, float value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    // 保存boolean型数据
    private static void setBoolean(Context context, String key, boolean b) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    // 保存String型数据
    private static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 保存Long型数据
    private static void setLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static SharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

}
