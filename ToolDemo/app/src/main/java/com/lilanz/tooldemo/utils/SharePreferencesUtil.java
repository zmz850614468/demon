package com.lilanz.tooldemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "sharedClothMeasure";
    private static final String PARTERN_ID = "parternId";   // 合作者Id

    private static SharedPreferences instance = null;


    // 获取合作者id
    public static int getParternId(Context context) {
        return getInstance(context).getInt(PARTERN_ID, 0);
    }

    // 保存合作者id
    public static void setParternId(Context context, int id) {
        setIntegre(context, PARTERN_ID, id);
    }

    //============================      以下为固定模式        =============================

    // 保存int型数据
    private static void setIntegre(Context context, String key, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(key, value);
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

    private static SharedPreferences getInstance(Context context) {
        if (instance == null) {
            instance = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return instance;
    }

}
