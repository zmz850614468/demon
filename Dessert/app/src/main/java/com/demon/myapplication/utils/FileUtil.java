package com.demon.myapplication.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
            File[] fileList = file.listFiles();
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

    /**
     * 获取存储路径
     *
     * @return 所有可用于存储的不同的卡的位置，用一个List来保存
     */
    public static List<String> getExtSDCardPathList() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        //首先判断一下外置SD卡的状态，处于挂载状态才能获取的到
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED) && extFile.exists() && extFile.isDirectory() && extFile.canWrite()) {
            //外置SD卡的路径
            paths.add(extFile.getAbsolutePath());
        }
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line
                        .contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                //扩展存储卡即TF卡或者SD卡路径
                paths.add(mountPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

}
