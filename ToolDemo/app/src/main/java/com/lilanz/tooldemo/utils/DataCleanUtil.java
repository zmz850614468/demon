package com.lilanz.tooldemo.utils;

import android.content.Context;

import java.io.File;

/**
 * 清除应用一下路径的所有数据
 * "data/data/" + context.getPackageName()
 */
public class DataCleanUtil {

    /**
     * 删除应用的所有数据
     *
     * @param context
     */
    public static void deleteAppAllData(Context context) {
        deleteFile(new File("data/data/" + context.getPackageName()));
    }

    /**
     * 删除指定文件夹下的所有数据
     *
     * @param file
     */
    private static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

}