package com.lilanz.tooldemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";
    private static final String PARTERN_ID = "parternId";   // 合作者Id

    private static SharedPreferences instance = null;


    // 获取合作者id
    public static int getParternId(Context context) {
        return getInstance(context).getInt(PARTERN_ID, 0);
    }

    // 保存合作者id
    public static void setParternId(Context context, int id) {
        setInteger(context, PARTERN_ID, id);
    }

    private static final String SELECT_BLE_ADDRESS = "selectedBleAddress";   //

    // 获取选择的蓝牙地址
    public static String getBleAddress(Context context) {
        return getInstance(context).getString(SELECT_BLE_ADDRESS, "");
    }

    public static void saveBleAddress(Context context, String address) {
        setString(context, SELECT_BLE_ADDRESS, address);
    }

    private static final String LAST_SAVE_DAY = "lastSaveDay";   //

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
