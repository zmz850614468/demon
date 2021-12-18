package com.demon.opencvbase.util;

import android.content.Context;

import java.io.File;

/**
 * 用于判断设备是否已经root
 */
public class RootUtil {

    /**
     * 判断设备是否已经被root
     *
     * @param context
     * @return
     */
    public static boolean isRoot(Context context) {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
//        String path = Environment.getExternalStorageDirectory().getPath();
//        path += "/root_check";
//
//        File file = new File(path);
//        try {
//            file.createNewFile();
//            file.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
    }
}
