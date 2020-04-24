package com.lilanz.camerademo.utils;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) ? true : false;
    }

    /**
     * 获取最大像素比例值
     * @param sizeList 摄像头支持的所有，像素比例值
     * @return 最大的像素比例值
     */
    public static Camera.Size getMaxSize(List<Camera.Size> sizeList){
        Camera.Size disSize = sizeList.get(0);
        for (Camera.Size size : sizeList) {
            if (size.width > disSize.width || size.height > disSize.height){
                disSize = size;
            }
        }
        return disSize;
    }

    /**
     * 保存bitmap图片到本地内存
     *
     * @param bmp
     * @param path：可以为空，默认到本地DCIM文件加中
     */
    public static void saveBitmap(Bitmap bmp, String path) {
        if (isEmpty(path)) {
            path = getPictureFile( ".png").getAbsolutePath();
        }
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
     * 获取图片保存路径：当前工程定制的：.png 图片
     */
    public static final File getPictureSavePath(String fileName) {
        // 子目录文件名："myPicture"
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "myPicture");
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, fileName + ".png");
        }
        return null;
    }

    /**
     * 获取图片保存路径：
     * 主目录：安卓系统的 DCIM 文件下
     * @param ext：可以传null值，图片文件的后缀名；默认是 ".png"
     * @return 图片文件的具体路径
     */
    public static final File getPictureFile(String ext) {
        if (isEmpty(ext)){
            ext = ".png";
        }
        // 子目录文件名："myPicture"
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "myPicture");
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, getDataTimeStr() + ext);
        }
        return null;
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd-HH-mm-ss"
     */
    public static String getDataTimeStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }


}
