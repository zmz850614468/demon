package com.lilanz.kotlintool.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.lang.Exception

class BuildUtil {
    companion object {
        /**
         * 获取应用版本号
         */
        fun getVersionCode(context: Context): Int {
            var manager: PackageManager = context.packageManager
            try {
                var info: PackageInfo = manager.getPackageInfo(context.packageName, 0)
                return info.versionCode
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        /**
         * 判断应用是否处于debug
         */
        fun isInDebug(context: Context): Boolean {
            try {
                var info: ApplicationInfo = context.applicationInfo
                return (info.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
            } catch (e: Exception) {
                return false
            }
        }
    }
}