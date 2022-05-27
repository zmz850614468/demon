package com.demon.fit.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";

    private static SharedPreferences instance = null;

    private static final String FU_LI = "fuLi";   //
    private static final String FU_LI_BASE = "fuLiBase";   //
    private static final String FU_LI_TIMES = "fuLTimes";   //
    private static final String OPERATE_TODAY = "operateToday";   //

    // 获取
    public static String getOperateToday(Context context) {
        return getInstance(context).getString(OPERATE_TODAY, null);
    }

    public static void saveOperateToday(Context context, String operateToday) {
        setString(context, OPERATE_TODAY, operateToday);
    }

    // 获取
    public static int getFuLTimes(Context context) {
        return getInstance(context).getInt(FU_LI_TIMES, 12);
    }

    public static void saveFuLTimes(Context context, int fuLTimes) {
        setInteger(context, FU_LI_TIMES, fuLTimes);
    }

    // 获取
    public static float getFuLBase(Context context) {
        return getInstance(context).getFloat(FU_LI_BASE, 1);
    }

    public static void saveFuLBase(Context context, float fuLBase) {
        setFloat(context, FU_LI_BASE, fuLBase);
    }

    // 获取
    public static int getFuLi(Context context) {
        return getInstance(context).getInt(FU_LI, 1);
    }

    public static void saveFuLi(Context context, int fuLi) {
        setInteger(context, FU_LI, fuLi);
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
