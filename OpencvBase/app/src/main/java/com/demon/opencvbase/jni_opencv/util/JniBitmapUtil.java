package com.demon.opencvbase.jni_opencv.util;

import android.graphics.Bitmap;

import com.demon.opencvbase.jni_opencv.jni.BitmapNative;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * 处理 jni图片
 */
public class JniBitmapUtil {

    /**
     * 图片灰度化
     *
     * @param bitmap
     * @return
     */
    public static Bitmap bitmapToGray(final Bitmap bitmap) {
//        Mat grayMat = new Mat();
//        BitmapNative.bitmap2gray(bitmap, grayMat.nativeObj);    // 调用jni函数，获取灰色图片mat
//        // 灰色图片mat转bitmap
//        Bitmap grayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
//        Utils.matToBitmap(grayMat, grayBitmap);
//        grayMat.release();

        Bitmap grayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        BitmapNative.bitmap2gray2(bitmap, grayBitmap);

        return grayBitmap;
    }
}
