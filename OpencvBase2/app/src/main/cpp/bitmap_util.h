//
// Created by zhmz on 2021/12/30.
//
#include <opencv/cv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/core/mat.hpp>
#include <android/bitmap.h>

#ifndef OPENCVBASE_BITMAP_UTIL_H
#define OPENCVBASE_BITMAP_UTIL_H

struct LinePoint {
    int x1;
    int y1;
    int x2;
    int y2;
};

cv::Mat lut(cv::Mat src, uchar divideWidth);

cv::Mat bitmap2Mat(JNIEnv *env, jobject bitmap);

bool mat2Bitmap(JNIEnv *env, cv::Mat &matrix, jobject obj_bitmap);

LinePoint calculateLine(int arr[][2], int rows, int clos);

#endif //OPENCVBASE_BITMAP_UTIL_H
