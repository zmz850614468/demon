package com.demon.opencvbase.jni_opencv.jni;

import android.graphics.Bitmap;

/**
 * jni 的图片处理
 */
public class BitmapNative {

    /**
     * 图片灰度化
     *
     * @param bitmap
     * @param grayAddr
     */
    public static native void bitmap2gray(Bitmap bitmap, long grayAddr);

    /**
     * 图片灰度化
     *
     * @param bitmap
     * @param grayBitmap
     */
    public static native void bitmap2gray2(Bitmap bitmap, Bitmap grayBitmap);

    /**
     * 图片查表处理，减少像素种类
     *
     * @param bitmap
     * @param divideValue
     * @param lutBitmap
     */
    public static native void bitmapLut(Bitmap bitmap, int divideValue, Bitmap lutBitmap);

    /**
     * 修改图片饱和度和亮度
     *
     * @param bitmap
     * @param alpha
     * @param beta
     * @param desBitmap
     */
    public static native void bitmapConverTo(Bitmap bitmap, double alpha, int beta, Bitmap desBitmap);

    /**
     * 对图片进行侵蚀操作
     *
     * @param bitmap
     * @param desBitmap
     */
    public static native void bitmapErode(Bitmap bitmap, Bitmap desBitmap);

    /**
     * 对图片进行扩展操作
     *
     * @param bitmap
     * @param desBitmap
     */
    public static native void bitmapDilate(Bitmap bitmap, Bitmap desBitmap);

    /**
     * 图片二值化处理
     *
     * @param bitmap
     * @param thresh    阈值（0~256）
     * @param type      类型：1~5
     *                  0:大于阈值的，为最大值；其他为0
     *                  1：大于阈值的，为0；其他为最大值
     *                  2:大于阈值的，为阈值；其他的为原值
     *                  3：大于阈值的，为0；其他的为原值
     *                  4:小于阈值的，为0；其他的为原值
     * @param desBitmap
     */
    public static native void bitmapThreshold(Bitmap bitmap, double thresh, int type, Bitmap desBitmap);

    /**
     * 图片中值模糊
     *
     * @param bitmap
     * @param kSize
     * @param desBitmap
     */
    public static native void bitmapBlur(Bitmap bitmap, int kSize, Bitmap desBitmap);

    /**
     * 图片中值模糊
     *
     * @param bitmap
     * @param kSize
     * @param desBitmap
     */
    public static native void bitmapMediaBlur(Bitmap bitmap, int kSize, Bitmap desBitmap);

    /**
     * 图片高斯模糊
     *
     * @param bitmap
     * @param kSize
     * @param desBitmap
     */
    public static native void bitmapGaussBlur(Bitmap bitmap, int kSize, Bitmap desBitmap);

    /**
     * 二值化处理：inRange 像素值范围检测
     *
     * @param bitmap
     * @param rMin
     * @param gMin
     * @param bMin
     * @param rMax
     * @param gMax
     * @param bMax
     * @param desBitmap
     */
    public static native void bitmapInRange(Bitmap bitmap, int rMin, int gMin, int bMin,
                                            int rMax, int gMax, int bMax, Bitmap desBitmap);

    /**
     * 对图片进行位操作
     *
     * @param src1
     * @param src2
     * @param type      1:and ; 2:or ; 3:nor ; 4:not
     * @param desBitmap
     */
    public static native void bitmapBitwise(Bitmap src1, Bitmap src2, int type, Bitmap desBitmap);

    /**
     * 把图片切分成三个通道
     *
     * @param bitmap
     * @param rBitmap
     * @param gBitmap
     * @param bBitmap
     */
    public static native void bitmapSplit(Bitmap bitmap, Bitmap rBitmap, Bitmap gBitmap, Bitmap bBitmap);

}
