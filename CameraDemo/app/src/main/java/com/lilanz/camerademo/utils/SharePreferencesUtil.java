package com.lilanz.camerademo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {

    private static final String SHARED_PREFERENCE_NAME = "takePicture";
//    private static final String CURRETN_VIDEL_PATH = "currentVideoPath";   // 当前录制视频的地址


    private static SharedPreferences instance = null;



//    // 保存当前视频录制的视频地址
//    public static void setCurrentVideoPath(Context context, String path){
//        setString(context, CURRETN_VIDEL_PATH, path);
//    }
//
//    // 获取当前视频录制的地址
//    public static String getCurrentVideoPath(Context context){
//        return getInstance(context).getString(CURRETN_VIDEL_PATH, "");
//    }



    //================================以下是通用的================================================

    // 保存int型数据
    private static void setIntegre(Context context, String key, int value) {
        SharedPreferences.Editor editor = getInstance(context).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // 保存String型数据
    private static void setString(Context context, String key, String value){
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
