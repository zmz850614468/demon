package com.demon.remotecontrol.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;


import com.demon.remotecontrol.bean.AppInfoBean;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用工具类
 */
public class AppUtil {

    /**
     * 安装应用
     *
     * @param context
     * @param file
     */
    public static void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 删除应用
     *
     * @param context
     * @param packageName
     */
    public static void uninstall(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 获取设备安装的应用
     *
     * @param context
     * @param isIncludeSystem 是否包含系统应用
     * @return
     */
    public static List<AppInfoBean> getInstallApp(Context context, boolean isIncludeSystem) {
        List<AppInfoBean> appInfoList = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // MATCH_UNINSTALLED_PACKAGES
        List<ApplicationInfo> applicationInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo app : applicationInfoList) {
            if (isIncludeSystem || (app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) { // 非系统应用
                appInfoList.add(getAppInfo(app, pm));
            }
        }

        return appInfoList;
    }

    /**
     * 获取应用信息
     *
     * @param app
     * @param pm
     * @return
     */
    private static AppInfoBean getAppInfo(ApplicationInfo app, PackageManager pm) {
        AppInfoBean bean = new AppInfoBean();
        bean.appName = pm.getApplicationLabel(app).toString();
//        bean.appDrawable = app.loadIcon(pm);
        bean.packageName = app.packageName;

        File file = new File(app.sourceDir);
        long size = file.length();
        DecimalFormat decimalFormat = new DecimalFormat("#");
        bean.appSize = decimalFormat.format(size / (1024 * 1024)) + "M";

        try {
            PackageInfo packageInfo = pm.getPackageInfo(app.packageName, 0);
            long lastUpdateTime = packageInfo.lastUpdateTime;
            bean.lastUpdateTime = lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return bean;
    }

}
