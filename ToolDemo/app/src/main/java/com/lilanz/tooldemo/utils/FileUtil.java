package com.lilanz.tooldemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    private static final String PICTURE_FILE_NAME = "恶天使魔";

    /**
     * 获取图片保存路径：
     * 主目录：安卓系统的 DCIM 文件下
     *
     * @return 图片文件的具体路径
     */
    public static File getPictureFile(Context context) {
        // 子目录文件名："myPicture"
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, StringUtil.getDataStr() + ".png");
        }
        return null;
    }

    /**
     * 获取视频保存路径：
     * 主目录：安卓系统的 Movies 文件下
     *
     * @return 图片文件的具体路径
     */
    public static File getVideoFile(Context context) {
        // 子目录文件名："myPicture"
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, StringUtil.getDataStr() + ".mp4");
        }
        return null;
    }

    /**
     * @param context
     * @param VideoName
     * @return
     */
    public static File getVideoFile(Context context, String VideoName) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, VideoName + ".mp4");
        }
        return null;
    }

    /**
     * 获取当前文件夹大小，不递归子文件夹
     *
     * @param file
     * @return
     */
    public static long getCurrentFolderSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    //跳过子文件夹

                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定文件夹下的所有数据
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
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

}
