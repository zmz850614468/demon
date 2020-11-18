package com.demon.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";

    private static SharedPreferences instance = null;

    private static final String DESSERT_LIST = "dessertList";   //

    private static final String DB_LAST_UPDATE = "dbLastUpdate";   //

    // 获取 数据库上次更新时间
    public static long getDBLastUpdate(Context context) {
        return getInstance(context).getLong(DB_LAST_UPDATE, 0);
    }

    public static void saveDBLastUpdate(Context context, long time) {
        setLong(context, DB_LAST_UPDATE, time);
    }

    // 获取
    public static List<String> getDessert(Context context) {
        List<String> list = null;
        String listJson = getInstance(context).getString(DESSERT_LIST, "[]");
        list = new Gson().fromJson(listJson, ArrayList.class);
        if (list == null) {
            list = new ArrayList<>();
        }

        return list;
    }

    public static void saveDessert(Context context, @NonNull List<String> list) {
        setString(context, DESSERT_LIST, new Gson().toJson(list));
    }

    public static void addDessert(Context context, @NonNull String str) {
        List<String> list = getDessert(context);
        if (!list.contains(str)) {
            list.add(str);
        }
        saveDessert(context, list);
    }

    public static void removeDessert(Context context, @NonNull String str) {
        List<String> list = getDessert(context);
        if (list.contains(str)) {
            list.remove(str);
        }
        saveDessert(context, list);
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
