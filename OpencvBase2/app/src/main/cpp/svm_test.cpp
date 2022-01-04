
/**
 * JNI 常用函数或方法
 */
#ifndef SVM_TEST_CPP
#define SVM_TEST_CPP

#include <stdlib.h>
#include <map>
#include <vector>
#include <string>
#include <jni.h>
#include <iostream>

#include <opencv/cv.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/core.hpp>
#include <opencv2/core/mat.hpp>
#include <android/bitmap.h>
#include "bitmap_util.h"

#include <stdio.h>
#include <time.h>
#include <opencv2/opencv.hpp>
#include <iostream>
#include <opencv/cv.h>
#include <opencv2/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>
#include <iostream>

/**
 * 输出
 */
#include <android/log.h>     // 日志输出

#define TAG    "jni-log" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型


/**
 * 命名空间
 */
using namespace std;
using namespace cv;

/**
 * svm 测试demo
 */
extern "C" JNIEXPORT void JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_LearningNative_svmTest(JNIEnv *env, jclass clazz,
                                                                 jobject bitmap,
                                                                 jobject des_bitmap) {
//    cv::Mat rgb = bitmap2Mat(env, bitmap);

//    int width = 512, height = 512;
//    Mat image = Mat::zeros(height, width, CV_8UC3);
    Mat image = bitmap2Mat(env, des_bitmap);

    // set up training data
//    float labels[4] = {1.0, 1.0, -1.0f, -1.0f};
    int labels[8] = {2, 2, 2, 2, 1, 1, 1, 1};
    Mat labelsMat(8, 1, CV_32SC1, labels);
//    Mat labelsMat(4, 1, CV_32FC1, labels);

    float trainingData[8][2] = {{501, 10},
                                {255, 10},
                                {100, 30},
                                {180, 50},
                                {300, 401},
                                {450, 301},
                                {501, 255},
                                {10,  501}};
    Mat trainingDataMat(8, 2, CV_32FC1, trainingData);


    // set up SVM's parameters
//    CvSVMParams params;
//    params.svm_type = CvSVM::C_SVC;
//    params.kernel_type = CvSVM::LINEAR;
//    params.term_crit = cvTermCriteria(CV_TERMCRIT_ITER, 100, 1e-6);

//----SVM训练
    Ptr<cv::ml::SVM> svm = ml::SVM::create();
    svm->setType(cv::ml::SVM::Types::C_SVC);
    svm->setKernel(cv::ml::SVM::KernelTypes::LINEAR);
//    svm->setDegree(3);
//    svm->setGamma(3);
//    svm->setCoef0(1.0);
//    svm->setC(10);
//    svm->setNu(0);
//    svm->setP(0.1);
    svm->setTermCriteria(cv::TermCriteria(cv::TermCriteria::MAX_ITER, 100, 1e-6));
    svm->train(trainingDataMat, cv::ml::SampleTypes::ROW_SAMPLE, labelsMat);


    // rgba
    Vec4b green(0, 255, 0, 255), blue(0, 0, 255, 255);

    for (int i = 0; i < 512; ++i) {
        for (int j = 0; j < 512; ++j) {

            Mat sampleMat(1, 2, CV_32FC1);
            sampleMat.at<float>(0, 0) = i * 1.0f;
            sampleMat.at<float>(0, 1) = j * 1.0f;

            // predict 函数使用训练好的SVM模型对一个输入的样本进行分类
            float response = svm->predict(sampleMat);

            if (response == 2) {
                // 注意这里是(j,i),不是(i,j)
                image.at<Vec4b>(j, i) = green;
            } else if (response == 1) {
                // 同上
                image.at<Vec4b>(j, i) = blue;
            }
        }
    }
    int thickness = -1;
    int lineType = 8;

    circle(image, Point(501, 10), 5, Scalar(0, 0, 0), thickness, lineType);
    circle(image, Point(255, 10), 5, Scalar(0, 0, 0), thickness, lineType);
    circle(image, Point(100, 30), 5, Scalar(0, 0, 0), thickness, lineType);
    circle(image, Point(180, 50), 5, Scalar(0, 0, 0), thickness, lineType);
    circle(image, Point(501, 255), 5, Scalar(255, 255, 255), thickness, lineType);
    circle(image, Point(10, 501), 5, Scalar(255, 255, 255), thickness, lineType);
    circle(image, Point(300, 401), 5, Scalar(255, 255, 255), thickness, lineType);
    circle(image, Point(450, 301), 5, Scalar(255, 255, 255), thickness, lineType);

    mat2Bitmap(env, image, des_bitmap);
    image.release();

}

#define count  80 // 这个是自定义的LOG的标识,最大240
extern "C" JNIEXPORT jboolean JNICALL
Java_com_demon_opencvbase_jni_1opencv_jni_LearningNative_svmBase(JNIEnv *env, jclass clazz) {


//    int labels[1] = {122};
//    Mat trainLabel(1, 1, CV_32SC1, labels);
//
//    Mat inImage;
//    string name = "/storage/emulated/0/Download/svm_1.jpg";
//    inImage = imread(name, IMREAD_UNCHANGED);
//    cvtColor(inImage, inImage, CV_BGRA2GRAY);   // 灰度处理
//    inImage = inImage.reshape(1, 1);

//    float data[1][3200] = {0};
//    for (int i = 0; i < 3200; i++) {
//        data[0][i] = inImage.at<uchar>(0, i);
//    }
//
//    Mat trainingDataMat(1, 3200, CV_32FC1, data);


    Ptr<cv::ml::SVM> svm = ml::SVM::create();
    svm->setType(cv::ml::SVM::Types::C_SVC);
    svm->setKernel(cv::ml::SVM::KernelTypes::LINEAR);
    svm->setTermCriteria(cv::TermCriteria(cv::TermCriteria::MAX_ITER, 1000, 1e-6));

    // 新版的 svm 训练
    Mat inImage;
    string name = "/storage/emulated/0/Download/svm_27.jpg";
    inImage = imread(name, IMREAD_UNCHANGED);
    cvtColor(inImage, inImage, CV_BGRA2GRAY);   // 灰度处理


//    static int count = 120;
//    int centerIndex = 122;
    int centerIndex = 195;
    float data[count][3200] = {0};
    int labels[count] = {0};
    for (int k = 0; k < count; ++k) {
        int move = k;
        if (move > centerIndex - 40) {
            move += 80;
        }
        int index = 0;
        for (int i = 0; i < inImage.rows; ++i) {
            for (int j = 0; j < inImage.cols; ++j) {
                data[k][index] = inImage.at<uchar>(i, (move + j) % inImage.cols);
                index++;
            }
        }


        int center = centerIndex - move;
        if (center < 0) {
            center += inImage.cols;
        }

        labels[k] = center;
    }
    Mat trainingDataMat(count, 3200, CV_32FC1, data);
    Mat trainLabel(count, 1, CV_32SC1, labels);

    svm->train(trainingDataMat, cv::ml::SampleTypes::ROW_SAMPLE, trainLabel);



    // 验证模型
    name = "/storage/emulated/0/Download/svm_1.jpg";
    inImage = imread(name, IMREAD_UNCHANGED);
    cvtColor(inImage, inImage, CV_BGRA2GRAY);   // 灰度处理

//    data[1][3200] = {0};
    for (int i = 0; i < 3200; i++) {
        data[0][i] = inImage.at<uchar>(0, i);
    }
    Mat trainingTestDataMat(1, 3200, CV_32FC1, data);

    float result = svm->predict(trainingTestDataMat);

    LOGD("result = %f", result);
//    int width = inImage.cols;
//    int height = inImage.rows;

    return true;
}


#endif