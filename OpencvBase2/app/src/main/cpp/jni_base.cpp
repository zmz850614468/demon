
/**
 * JNI 常用函数或方法
 */
#ifndef JNI_BASE_CPP
#define JNI_BASE_CPP

#include <stdlib.h>
#include <map>
#include <vector>
#include <string>
#include <jni.h>
#include <iostream>

/**
 * C++ 在线教程
 * https://www.w3cschool.cn/cpp/cpp-i6da2pq0.html
 */

/**
 * 命名空间
 */
using namespace std;

/**
 * 结构体
 */
struct Bean {
    int age;
    string name;
} bean, bean1;

void jniBase() {

    /**
     * 初始化，内部数值是遗留值
     */
    int intArr[3][2];
    intArr[1][1] = 13;

    /**
     * map 对象，可以直接赋值使用
     */
    map<int, string> baseMap;
    baseMap[15] = "name";

    /**
     * map 遍历
     */
    map<int, string>::iterator itHead = baseMap.begin();
    map<int, string>::iterator itEnd = baseMap.end();
    while (itHead != itEnd) {
        cout << itHead->first << ' ' << itHead->second << endl;
        itHead++;
    }

    /**
     * vector 顺序容器
     */
    vector<Bean> beanVec;
    beanVec.push_back(bean);    // 尾部添加
    vector<Bean>::iterator itVec = beanVec.begin();
    beanVec.insert(itVec, bean1);   // 指定位置插入

    /**
     * vector 遍历 ； 也可以用迭代器
     */
    for (int i = 0; i < beanVec.size(); ++i) {
        beanVec.at(i);
    }

    /**
     * 输出模式 : 可以连续输出
     */
    cout << "输出内容" << baseMap[15];
}

/**
 * 强转类型
 */
#include <stdio.h>
#include <sstream>      // 保留小数位数 转字符串
#include <opencv2/opencv.hpp>

void baseTypeTo(JNIEnv *env) {
    double x = 0.1f;
    double y = 0.2f;

    // 引用类型 必须初始化且无法修改
    double &xx = x;

    // 转 整形
    static_cast<int>(x);

    // 转 字符串 ; 字符串拼接
    string strX = to_string(x);
    string strY = to_string(y);
    string str = strX + strY + "end"; // 字符串 + 字符

    // 字符串 转 jstring
    env->NewStringUTF(str.c_str());

    // 保留小数位数
    ostringstream oss;
    oss << setprecision(2) << x << "&";
    oss << setprecision(2) << y;
    oss.str().c_str();

}


/**
 * 输出
 */
#include <android/log.h>     // 日志输出

#define TAG    "jni-log" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型

void output() {
    LOGD("%d", 12);
    LOGD("ljdsfjsdlfkjl");
}

#endif