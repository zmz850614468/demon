package com.demon.opencvbase.jni_opencv.jni;

import android.graphics.Bitmap;

public class AgvDealNative {

    /**
     * Agv小车图片处理
     *
     * @param bitmap
     */
    public static native String bitmapDeal(Bitmap bitmap);


    /**
     * 用于图片测试用
     *
     * @param bitmap
     * @param desBitmap
     * @return
     */
    public static native String bitmapTest(Bitmap bitmap, Bitmap desBitmap);

    public static native String video2pic(String file);
}
