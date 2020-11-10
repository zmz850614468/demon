package com.example.timeup.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";

    private static SharedPreferences instance = null;


    private static final String DB_LAST_UPDATE = "dbLastUpdate";   //

    // 获取 数据库上次更新时间
    public static long getDBLastUpdate(Context context) {
        return getInstance(context).getLong(DB_LAST_UPDATE, 0);
    }

    public static void saveDBLastUpdate(Context context, long time) {
        setLong(context, DB_LAST_UPDATE, time);
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
