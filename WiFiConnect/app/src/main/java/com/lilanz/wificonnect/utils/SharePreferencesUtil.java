package com.lilanz.wificonnect.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lilanz.wificonnect.controls.MediaControl;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";
    private static final String PARTERN_ID = "parternId";   // 合作者Id
    private static final String LAST_SAVE_DAY = "lastSaveDay";   //
    private static final String BECOME = "become";   // 服务端或是客服端
    private static final String SERVICE_IP = "serviceIp";   //
    private static final String INSIDE_SERVICE_IP = "insideServiceIp";   //
    private static final String SERVICE_PORT = "servicePort";   //
    private static final String SELECTED_IP_TYPE = "selectedIpType";   //
    private static final String STOP_MODE = "stopMode";   // 歌曲停止模式
    private static final String STOP_TIME = "stopTime";   // X分钟后停止播放
    private static final String STOP_SONG_COUNT = "stopSongCount";   // X首歌后停止播放
    private static final String LUYQ_NAME = "luYQName";   // 路由器名称
    private static final String LUYQ_PWD = "luYQPwd";   // 路由器密码

    private static SharedPreferences instance = null;


    // 获取 路由器密码
    public static String getLuYQPwd(Context context) {
        return getInstance(context).getString(LUYQ_PWD, "");
    }

    public static void saveLuYQPwd(Context context, String luYQPwd) {
        setString(context, LUYQ_PWD, luYQPwd);
    }

    // 获取 路由器名称
    public static String getLuYQName(Context context) {
        return getInstance(context).getString(LUYQ_NAME, "");
    }

    public static void saveLuYQName(Context context, String luYQName) {
        setString(context, LUYQ_NAME, luYQName);
    }

    // 获取
    public static int getStopSongCount(Context context) {
        return getInstance(context).getInt(STOP_SONG_COUNT, 10);
    }

    public static void saveStopSongCount(Context context, int songCount) {
        setInteger(context, STOP_SONG_COUNT, songCount);
    }

    // 获取
    public static int getStopTime(Context context) {
        return getInstance(context).getInt(STOP_TIME, 30);
    }

    public static void saveStopTime(Context context, int stopTime) {
        setInteger(context, STOP_TIME, stopTime);
    }

    // 获取
    public static int getStopMode(Context context) {
        return getInstance(context).getInt(STOP_MODE, MediaControl.STOP_NONE);
    }

    public static void saveStopMode(Context context, int stopMode) {
        setInteger(context, STOP_MODE, stopMode);
    }


    // 获取 选择的网络IP
    public static String getSelectedIpType(Context context) {
        return getInstance(context).getString(SELECTED_IP_TYPE, "局域网");
    }

    public static void saveSelectedIpType(Context context, String selectedIp) {
        setString(context, SELECTED_IP_TYPE, selectedIp);
    }

    // 获取 局域网服务器地址
    public static String getInsideServiceIp(Context context) {
        return getInstance(context).getString(INSIDE_SERVICE_IP, "192.168.1.101");
    }

    public static void saveInsideServiceIp(Context context, String insideServiceIp) {
        setString(context, INSIDE_SERVICE_IP, insideServiceIp);
    }

    // 获取 服务器端口
    public static int getServicePort(Context context) {
        return getInstance(context).getInt(SERVICE_PORT, 48539);
    }

    public static void saveServicePort(Context context, int servicePort) {
        setInteger(context, SERVICE_PORT, servicePort);
    }

    // 获取 广域网服务器地址
    public static String getServiceIp(Context context) {
//        return getInstance(context).getString(SERVICE_IP, "103.46.128.45");
        return getInstance(context).getString(SERVICE_IP, "3561rb3067.wicp.vip");
    }

    public static void saveServiceIp(Context context, String serviceIp) {
        setString(context, SERVICE_IP, serviceIp);
    }

    // 获取
    public static String getBecome(Context context) {
        return getInstance(context).getString(BECOME, "");
    }

    public static void saveBecome(Context context, String become) {
        setString(context, BECOME, become);
    }

    // 获取
    public static String getLastSaveDay(Context context) {
        return getInstance(context).getString(LAST_SAVE_DAY, "");
    }

    public static void saveLastSaveDay(Context context, String dayStr) {
        setString(context, LAST_SAVE_DAY, dayStr);
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
