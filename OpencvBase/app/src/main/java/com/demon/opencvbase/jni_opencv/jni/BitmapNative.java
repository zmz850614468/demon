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
     * 图片模板匹配
     *
     * @param bitmap    源图片
     * @param temp      模板图片
     * @param type      匹配类型
     *                  0-cv::TM_SQDIFF:使用平方差进行匹配，因此最佳的匹配结果在结果为0处，值越大匹配结果越差。
     *                  1-cv::TM_SQDIFF_NORMED:使用归一化的平方差进行匹配，最佳匹配也在结果为0处。
     *                  2-cv::TM_CCORR:相关性匹配方法，该方法使用源图像与模板图像的卷积结果进行匹配，因此，最佳匹配位置在值最大处，值越小匹配结果越差。
     *                  3-cv::TM_CCORR_NORMED:归一化的相关性匹配方法，与相关性匹配方法类似，最佳匹配位置也是在值最大处。
     *                  4-cv::TM_CCOEFF:相关性系数匹配方法，该方法使用源图像与其均值的差、模板与其均值的差二者之间的相关性进行匹配，最佳匹配结果在值等于1处，最差匹配结果在值等于-1处，值等于0直接表示二者不相关。
     *                  5-cv::TM_CCOEFF_NORMED:归一化的相关性系数匹配方法，正值表示匹配的结果较好，负值则表示匹配的效果较差，也是值越大，匹配效果也好。
     * @param desBitmap 输出匹配结果
     */
    public static native void bitmapMatchTemplate(Bitmap bitmap, Bitmap temp, int type, Bitmap desBitmap);

    /**
     * 图片直线检测
     *
     * @param bitmap
     * @param threshold 阈值
     * @param type      1: HoughLines(直线) ; 2: HoughLinesP(线段)
     * @param desBitmap
     */
    public static native void bitmapHoughLines(Bitmap bitmap, int threshold, int min, int type, Bitmap desBitmap);

    /**
     * 图片圆形检测
     *
     * @param bitmap
     * @param min
     * @param max
     * @param desBitmap
     */
    public static native void bitmapHoughCircles(Bitmap bitmap, int min, int max, Bitmap desBitmap);

    /**
     * 图片边缘检测
     *
     * @param bitmap
     * @param lowThreshold
     * @param desBitmap
     */
    public static native void bitmapCanny(Bitmap bitmap, int lowThreshold, Bitmap desBitmap);

    /**
     * 轮廓查找和绘制
     *
     * @param bitmap
     * @param desBitmap
     */
    public static native void bitmapContours(Bitmap bitmap, Bitmap desBitmap);

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
