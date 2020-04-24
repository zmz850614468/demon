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
     * @return 获取系统时间格式："yyyy-MM-dd-HH-mm-ss"
     */
    public static String getDataStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }


}
