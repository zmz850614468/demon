package com.demon.opencvbase.jni_opencv.jni;

import android.graphics.Bitmap;

/**
 * 用于机器学习
 */
public class LearningNative {

//    /**
//     * 对图片进行切割
//     *
//     * @param bitmap
//     * @param lineCount
//     * @param index
//     */
//    public static native void cutPic(Bitmap bitmap, int lineCount, int index);

    public static native void svmTest(Bitmap bitmap, Bitmap desBitmap);

    public static native boolean svmBase();
}
