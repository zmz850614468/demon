//
// Created by zhmz on 2021/4/26.
//

#ifndef BITMAP_UTIL_HPP
#define BITMAP_UTIL_HPP

#include <stdlib.h>
#include <opencv/cv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/core/mat.hpp>
#include <android/bitmap.h>

#define ASSERT(status, ret)     if (!(status)) { return ret; }
#define ASSERT_FALSE(status)    ASSERT(status, false)

cv::Mat lut(cv::Mat src, uchar divideWidth);

cv::Mat bitmap2Mat(JNIEnv *env, jobject bitmap);

bool mat2Bitmap(JNIEnv *env, cv::Mat &matrix, jobject obj_bitmap);
/**
 * 图片灰度化
 */
extern "C" JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmap2gray(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap, jlong grayAddr) {
    cv::Mat rgb = bitmap2Mat(env, bitmap);

    cv::Mat *gray = (cv::Mat *) grayAddr;
    cv::cvtColor(rgb, *gray, CV_BGRA2GRAY);

    AndroidBitmap_unlockPixels(env, bitmap);
}

/**
 * 图片灰度化
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmap2gray2(JNIEnv *env, jclass clazz,
                                                                    jobject bitmap,
                                                                    jobject gray_bitmap) {
    cv::Mat rgb = bitmap2Mat(env, bitmap);
    cv::Mat des = bitmap2Mat(env, gray_bitmap);

    cv::cvtColor(rgb, des, CV_BGRA2GRAY);
    mat2Bitmap(env, des, gray_bitmap);
    rgb.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapContours(JNIEnv *env, jclass clazz,
                                                                      jobject bitmap,
                                                                      jobject des_bitmap) {
    cv::RNG rng(12345);
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des = bitmap2Mat(env, des_bitmap);
    cv::Mat temp;
    cv::cvtColor(src, temp, CV_BGRA2GRAY);

    std::vector<std::vector<cv::Point> > contours;
    std::vector<cv::Vec4i> hierarchy;
    findContours(temp, contours, hierarchy, cv::RETR_TREE, cv::CHAIN_APPROX_SIMPLE,
                 cv::Point(0, 0));
    for (size_t i = 0; i < contours.size(); i++) {
        cv::Scalar color = cv::Scalar(rng.uniform(0, 255), rng.uniform(0, 255),
                                      rng.uniform(0, 255), 255);
        drawContours(des, contours, (int) i, color, 2, 8, hierarchy, 0, cv::Point());
    }

    src.release();
    des.release();
    temp.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapMatchTemplate(JNIEnv *env,
                                                                           jclass clazz,
                                                                           jobject bitmap,
                                                                           jobject temp, jint type,
                                                                           jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat templat = bitmap2Mat(env, temp);
    cv::Mat des = bitmap2Mat(env, des_bitmap);
    cv::Mat ftemp;

    double minVal;
    double maxVal;
    cv::Point minLoc;
    cv::Point maxLoc;
    cv::matchTemplate(src, templat, ftemp, type);
    cv::normalize(ftemp, ftemp, 1, 0, cv::NORM_MINMAX);
    cv::minMaxLoc(ftemp, &minVal, &maxVal, &minLoc, &maxLoc); // 查找最佳匹配点

    if (type == 0 || type == 1) {    // 最小值匹配
        rectangle(des, cv::Rect(minLoc.x, minLoc.y, templat.cols, templat.rows), 1, 3, 0);
    } else {     // 最大值匹配
        rectangle(des, cv::Rect(maxLoc.x, maxLoc.y, templat.cols, templat.rows), 1, 3, 0);
    }

    src.release();
    templat.release();
    des.release();
    ftemp.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapHoughCircles(JNIEnv *env, jclass clazz,
                                                                          jobject bitmap,
                                                                          jint min, jint max,
                                                                          jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::Mat res = bitmap2Mat(env, des_bitmap);
    cv::cvtColor(src, des, CV_BGRA2GRAY);

    std::vector<cv::Vec3f> circles;
    cv::HoughCircles(des, circles, cv::HOUGH_GRADIENT, 1,
                     des.rows /
                     16, // change this value to detect circles with different distances to each other
                     100, 30, min, max // change the last two parameters
            // (min_radius & max_radius) to detect larger circles
    );
    for (size_t i = 0; i < circles.size(); i++) {
        cv::Vec3i c = circles[i];
        cv::circle(res, cv::Point(c[0], c[1]), c[2], cv::Scalar(0, 0, 0, 255), 3, cv::LINE_AA);
        cv::circle(res, cv::Point(c[0], c[1]), 2, cv::Scalar(0, 0, 0, 255), 3, cv::LINE_AA);
    }

//    mat2Bitmap(env, res, des_bitmap);
    src.release();
    des.release();
    res.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapHoughLines(JNIEnv *env, jclass clazz,
                                                                        jobject bitmap,
                                                                        jint threshold, jint min,
                                                                        jint type,
                                                                        jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat outp = src.clone();
    cv::Mat des(src.size(), src.type());

    cv::cvtColor(src, des, CV_BGRA2GRAY);
    if (type == 1) {
        std::vector<cv::Vec2f> lines;
        cv::HoughLines(des, lines, 1, CV_PI / 180, threshold, 0, 0);
        for (size_t i = 0; i < lines.size(); i++) {
            float rho = lines[i][0], theta = lines[i][1];
            cv::Point pt1, pt2;
            double a = cos(theta), b = sin(theta);
            double x0 = a * rho, y0 = b * rho;
            pt1.x = cvRound(x0 + 1000 * (-b));
            pt1.y = cvRound(y0 + 1000 * (a));
            pt2.x = cvRound(x0 - 1000 * (-b));
            pt2.y = cvRound(y0 - 1000 * (a));
            cv::line(outp, pt1, pt2, cv::Scalar(0, 0, 255, 255), 3, CV_AA);
        }
    } else if (type == 2) {
        std::vector<cv::Vec4i> lines;
        cv::HoughLinesP(des, lines, 1, CV_PI / 180, threshold, min, 10);
        for (size_t i = 0; i < lines.size(); i++) {
            cv::Vec4i l = lines[i];
            cv::line(outp, cv::Point(l[0], l[1]), cv::Point(l[2], l[3]), cv::Scalar(0, 0, 255), 3,
                     cv::LINE_AA);
        }
    }
//    cv::Canny(src, des, low_threshold, low_threshold * 3);

//    cv::bitwise_and(src, des, des);

    mat2Bitmap(env, outp, des_bitmap);
    src.release();
    des.release();
    outp.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapCanny(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap,
                                                                   jint low_threshold,
                                                                   jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
//    cv::cvtColor(src, des, CV_BGRA2GRAY);
    cv::Canny(src, des, low_threshold, low_threshold * 3);

    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapBitwise(JNIEnv *env, jclass clazz,
                                                                     jobject src1, jobject src2,
                                                                     jint type,
                                                                     jobject des_bitmap) {
    cv::Mat matSrc1 = bitmap2Mat(env, src1);
    cv::Mat des(matSrc1.size(), matSrc1.type());
    if (type == 1 || type == 2 || type == 3) {
        cv::Mat matSrc2 = bitmap2Mat(env, src2);
        switch (type) {
            case 1:
                cv::bitwise_and(matSrc1, matSrc2, des);
                break;
            case 2:
                cv::bitwise_or(matSrc1, matSrc2, des);
                break;
            case 3:
                cv::bitwise_xor(matSrc1, matSrc2, des);
                break;
        }
        matSrc2.release();
    } else if (type == 4) {
        cv::bitwise_not(matSrc1, des);
    }

    mat2Bitmap(env, des, des_bitmap);
    matSrc1.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapInRange(JNIEnv *env, jclass clazz,
                                                                     jobject bitmap, jint r_min,
                                                                     jint g_min, jint b_min,
                                                                     jint r_max, jint g_max,
                                                                     jint b_max,
                                                                     jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::cvtColor(src, des, CV_BGR2HSV);
    cv::inRange(des, cv::Scalar(r_min, g_min, b_min), cv::Scalar(r_max, g_max, b_max), des);

//    cv::cvtColor(des, des, CV_GRAY2BGRA);
//    cv::bitwise_and(src, des, des);

    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();

}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapMediaBlur(JNIEnv *env, jclass clazz,
                                                                       jobject bitmap, jint k_size,
                                                                       jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::medianBlur(src, des, k_size);
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}
extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapGaussBlur(JNIEnv *env, jclass clazz,
                                                                       jobject bitmap, jint k_size,
                                                                       jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::GaussianBlur(src, des, cv::Size(k_size, k_size), 0, 0);
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}


extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapBlur(JNIEnv *env, jclass clazz,
                                                                  jobject bitmap, jint k_size,
                                                                  jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::blur(src, des, cv::Size(k_size, k_size));
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapThreshold(JNIEnv *env, jclass clazz,
                                                                       jobject bitmap,
                                                                       jdouble thresh, jint type,
                                                                       jobject des_bitmap) {
    cv::Mat rgb = bitmap2Mat(env, bitmap);
    cv::Mat gray(rgb.size(), CV_8UC1);
    cv::cvtColor(rgb, gray, CV_BGRA2GRAY);
    cv::threshold(gray, gray, thresh, 255, type);
    mat2Bitmap(env, gray, des_bitmap);
    gray.release();
    rgb.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapErode(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap,
                                                                   jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Point(3, 3));
    cv::erode(src, des, kernel);
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapDilate(JNIEnv *env, jclass clazz,
                                                                    jobject bitmap,
                                                                    jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    cv::Mat kernel = cv::getStructuringElement(cv::MORPH_RECT, cv::Point(3, 3));
    cv::dilate(src, des, kernel);
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapConverTo(JNIEnv *env, jclass clazz,
                                                                      jobject bitmap, jdouble alpha,
                                                                      jint beta,
                                                                      jobject des_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    cv::Mat des(src.size(), src.type());
    src.convertTo(des, -1, alpha, beta);    // 修改图片的饱和度和亮度
    mat2Bitmap(env, des, des_bitmap);
    src.release();
    des.release();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapLut(JNIEnv *env, jclass clazz,
                                                                 jobject bitmap, jint divide_value,
                                                                 jobject lut_bitmap) {
    cv::Mat src = bitmap2Mat(env, bitmap);
    src = lut(src, static_cast<uchar>(divide_value));
    mat2Bitmap(env, src, lut_bitmap);
    src.release();
}

/**
 * 把图片分成三通道图片
 */
extern "C"
JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_BitmapNative_bitmapSplit(JNIEnv *env, jclass clazz,
                                                                   jobject bitmap, jobject r_bitmap,
                                                                   jobject g_bitmap,
                                                                   jobject b_bitmap) {
    cv::Mat mat = bitmap2Mat(env, bitmap);

    cv::Mat *mv = new cv::Mat[mat.channels()];
    for (int i = 0; i < mat.channels(); ++i) {
        mv[i] = mat.clone();
    }

    cv::split(mat, mv);
    if (mat.channels() >= 3) {
        mat2Bitmap(env, mv[0], r_bitmap);
        mat2Bitmap(env, mv[1], g_bitmap);
        mat2Bitmap(env, mv[2], b_bitmap);
    }

    // 获取图片均值和平方差
//    cv::Mat mean, dev;
//    cv::meanStdDev(mv[0], mean, dev);
//    double bm = mean.at<double>(0,0);
//    double bd = dev.at<double>(0,0);

    for (int i = 0; i < mat.channels(); ++i) {
        mv[i].release();
    }
    mat.release();
}

/**
 * 通过查表法，减少颜色种类
 * @param src
 * @param divideWidth
 * @return
 */
cv::Mat lut(cv::Mat src, uchar divideWidth) {
    uchar *lutData = new uchar[256];
    for (int i = 0; i < 256; ++i) {
        lutData[i] = static_cast<uchar>(divideWidth * (i / divideWidth));
    }
    cv::Mat table(1, 256, CV_8UC1, lutData);

    cv::Mat des(src.size(), src.type());
    cv::LUT(src, table, des);
    return des;
}

/**
 * bitmap 转 Mat (4通道)
 * @param env
 * @param bitmap
 * @return
 */
cv::Mat bitmap2Mat(JNIEnv *env, jobject bitmap) {
    AndroidBitmapInfo bmpInfo;

    int ret = 0;
    ret = AndroidBitmap_getInfo(env, bitmap, &bmpInfo);
//    if (ret < 0) {
//        return nullptr;
//    }

    int *pixels = NULL;
    ret = AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels);
//    if (ret < 0 || pixels == NULL) {
//        return nullptr;
//    }

    cv::Mat rgb(cv::Size(bmpInfo.width, bmpInfo.height), CV_8UC4, pixels);
    return rgb;
}

/**
 * Mat 转 bitmap
 * @param env
 * @param matrix
 * @param obj_bitmap
 * @return
 */
bool mat2Bitmap(JNIEnv *env, cv::Mat &matrix, jobject obj_bitmap) {
    void *bitmapPixels;                                            // 保存图片像素数据
    AndroidBitmapInfo bitmapInfo;                                   // 保存图片参数

    ASSERT_FALSE(AndroidBitmap_getInfo(env, obj_bitmap, &bitmapInfo) >= 0);        // 获取图片参数
    ASSERT_FALSE(bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888
                 || bitmapInfo.format ==
                    ANDROID_BITMAP_FORMAT_RGB_565);          // 只支持 ARGB_8888 和 RGB_565
    ASSERT_FALSE(matrix.dims == 2
                 && bitmapInfo.height == (uint32_t) matrix.rows
                 && bitmapInfo.width == (uint32_t) matrix.cols);                   // 必须是 2 维矩阵，长宽一致
    ASSERT_FALSE(matrix.type() == CV_8UC1 || matrix.type() == CV_8UC3 || matrix.type() == CV_8UC4);
    ASSERT_FALSE(AndroidBitmap_lockPixels(env, obj_bitmap, &bitmapPixels) >= 0);  // 获取图片像素（锁定内存块）
    ASSERT_FALSE(bitmapPixels);

    if (bitmapInfo.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        cv::Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC4, bitmapPixels);
        switch (matrix.type()) {
            case CV_8UC1:
                cv::cvtColor(matrix, tmp, cv::COLOR_GRAY2RGBA);
                break;
            case CV_8UC3:
                cv::cvtColor(matrix, tmp, cv::COLOR_RGB2RGBA);
                break;
            case CV_8UC4:
                matrix.copyTo(tmp);
                break;
            default:
                AndroidBitmap_unlockPixels(env, obj_bitmap);
                return false;
        }
    } else {
        cv::Mat tmp(bitmapInfo.height, bitmapInfo.width, CV_8UC2, bitmapPixels);
        switch (matrix.type()) {
            case CV_8UC1:
                cv::cvtColor(matrix, tmp, cv::COLOR_GRAY2BGR565);
                break;
            case CV_8UC3:
                cv::cvtColor(matrix, tmp, cv::COLOR_RGB2BGR565);
                break;
            case CV_8UC4:
                cv::cvtColor(matrix, tmp, cv::COLOR_RGBA2BGR565);
                break;
            default:
                AndroidBitmap_unlockPixels(env, obj_bitmap);
                return false;
        }
    }
    AndroidBitmap_unlockPixels(env, obj_bitmap);                // 解锁
    return true;
}

#endif
