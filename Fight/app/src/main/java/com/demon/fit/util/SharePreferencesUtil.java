package com.demon.fit.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.demon.fit.bean.FuLiBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "shared";

    private static SharedPreferences instance = null;

    private static final String FU_LI = "fuLi";   //
    private static final String FU_LI_BASE = "fuLiBase";   //
    private static final String FU_LI_TIMES = "fuLTimes";   //
    private static final String OPERATE_TODAY = "operateToday";   //

    private static final String SELECTED_COUNT = "selectedCount";   //
    private static final String PRICE_LIST = "priceList";   //
    private static final String VOICE_TIP = "voiceTip";   //
    private static final String VIBRATOR_TIP = "vibratorTip";   //

    private static final String FULI_RECORD = "fuLiRecode";   //

    // 获取
    public static List<FuLiBean> getFuLiRecode(Context context) {
        String msg = getInstance(context).getString(FULI_RECORD, "[]");
        return new Gson().fromJson(msg, new TypeToken<ArrayList<FuLiBean>>() {
        }.getType());
    }

    public static void saveFuLiRecode(Context context, List<FuLiBean> list) {
        setString(context, FULI_RECORD, new Gson().toJson(list));
    }

    // 获取
    public static boolean getVibratorTip(Context context) {
        return getInstance(context).getBoolean(VIBRATOR_TIP, false);
    }

    public static void saveVibratorTip(Context context, boolean vibratorTip) {
        setBoolean(context, VIBRATOR_TIP, vibratorTip);
    }

    // 获取
    public static boolean getVoiceTip(Context context) {
        return getInstance(context).getBoolean(VOICE_TIP, true);
    }

    public static void saveVoiceTip(Context context, boolean voiceTip) {
        setBoolean(context, VOICE_TIP, voiceTip);
    }

    // 获取
    public static List<String> getPriceList(Context context) {
        String priceLit = getInstance(context).getString(PRICE_LIST, "[]");
        return new Gson().fromJson(priceLit, new TypeToken<ArrayList<String>>() {
        }.getType());
    }

    public static void savePriceList(Context context, List<String> priceList) {
        setString(context, PRICE_LIST, new Gson().toJson(priceList));
    }

    // 获取
    public static int getSelectedCount(Context context) {
        return getInstance(context).getInt(SELECTED_COUNT, 30);
    }

    public static void saveSelectedCount(Context context, int selectedCount) {
        setInteger(context, SELECTED_COUNT, selectedCount);
    }

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
