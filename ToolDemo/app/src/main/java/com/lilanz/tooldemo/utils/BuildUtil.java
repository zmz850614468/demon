package com.lilanz.tooldemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * 打包相关方法
 */
public class BuildUtil {


    /**
     * 获得应用版本名
     */
    public static String getVersionName(final Context con) {
        return getPackgeInfo(con).versionName;
    }

    /**
     * 获得应用版本号
     */
    public static int getVersionCode(final Context con) {
        return getPackgeInfo(con).versionCode;
    }

    /**
     * 获得应用包名
     */
    public static String getPackageName(final Context con) {
        return getPackgeInfo(con).packageName;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取应用包相关信息
     */
    public static PackageInfo getPackgeInfo(final Context con) {
        PackageManager packageManager = con.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(con.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("BuildUtil", e.toString());
        }
        return packageInfo;
    }

}
