package com.lilanz.doublecamera.utils;

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
     * 保存字节图片
     *
     * @param context
     * @param imageBytes    字节图片数据
     * @param path          保存的图片路径
     */
    public static void saveImageByte(Context context, byte[] imageBytes, String path) {
        Bitmap temp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        FileUtil.saveBitmap(context, temp, path);
    }

    /**
     * 保存bitmap图片到本地内存
     *
     * @param context
     * @param bmp
     */
    public static void saveBitmap(Context context, Bitmap bmp) {
        String path = getPictureFile(context).getAbsolutePath();
        saveBitmap(context, bmp, path);
    }

    /**
     * 保存bitmap图片到本地内存
     *
     * @param bmp
     * @param path：可以为空，默认到本地DCIM文件加中
     */
    public static void saveBitmap(Context context, Bitmap bmp, String path) {
        File outFile = new File(path);
        BufferedOutputStream outStream = null;
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(outFile));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
     *
     * @param context
     * @param VideoName
     * @return
     */
    public static File getVideoFile(Context context, String VideoName){
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, VideoName + ".mp4");
        }
        return null;
    }

}
