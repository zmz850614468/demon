package com.demon.opencvbase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.demon.opencvbase.jni_opencv.jni.LearningNative;
import com.demon.opencvbase.util.StringUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private void showLog(String msg) {
        Log.e("ExampleUnitTest", msg);
    }

    @Test
    public void addition_isCorrect() {
        boolean is = LearningNative.svmBase();
        showLog("建模结果：" + is);

        int weight = 20000;

//        String hexWeight = StringUtil.int2HexString(weight);
//        int count = hexWeight.length();
//        for (int i = 0; i < 6 - count; i++) {
//            hexWeight = "0" + hexWeight;
//        }

//        String hexWeight = String.format("%06x", weight);
//        hexWeight = "00 63 25 " + hexWeight;
//
//        String lcr = LCR(hexWeight);
//        for (int i = 0; i < 2 - lcr.length(); i++) {
//            lcr = "0" + lcr;
//        }
//
//        hexWeight += lcr;

    }

    /**
     * LCR校验结果
     *
     * @param hexStr
     * @return
     */
    public static String LCR(String hexStr) {
        hexStr = hexStr.replace(" ", "");
        if (hexStr.length() % 2 != 0) {
            return "-1";
        }
        int count = 0;
        for (int i = 0; i < hexStr.length(); i = i + 2) {
            count += hexStr2Int(hexStr.substring(i, i + 2));
        }

        count = count % 256;
        return int2HexString(count);
    }

    /**
     * 数值转成16进制的字符串，
     *
     * @param num
     * @return
     */
    public static String int2HexString(int num) {
        StringBuffer buffer = new StringBuffer();
        int temp = 0;

        do {
            temp = num % 16;    // 余数
            num = num / 16;
            switch (temp) {
                case 15:
                    buffer.insert(0, "F");
                    break;
                case 14:
                    buffer.insert(0, "E");
                    break;
                case 13:
                    buffer.insert(0, "D");
                    break;
                case 12:
                    buffer.insert(0, "C");
                    break;
                case 11:
                    buffer.insert(0, "B");
                    break;
                case 10:
                    buffer.insert(0, "A");
                    break;
                default:
                    buffer.insert(0, temp);
                    break;
            }

        } while (num > 0);

        return buffer.toString();
    }

    /**
     * @param hexStr 屏幕端显示的16进制数据
     * @return 屏幕端显示的10进制数据
     */
    public static int hexStr2Int(@NonNull String hexStr) {
        hexStr = hexStr.replace(" ", "");
        int count = 0;
        int num = 0;
        for (char c : hexStr.toCharArray()) {
            switch (c) {
                case 'a':
                case 'A':
                    num = 10;
                    break;
                case 'b':
                case 'B':
                    num = 11;
                    break;
                case 'c':
                case 'C':
                    num = 12;
                    break;
                case 'd':
                case 'D':
                    num = 13;
                    break;
                case 'e':
                case 'E':
                    num = 14;
                    break;
                case 'f':
                case 'F':
                    num = 15;
                    break;
                default:
                    num = c - '0';
                    break;
            }
            count = count * 16 + num;
        }
        return count;
    }


}