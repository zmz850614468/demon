package com.lilanz.tooldemo.utils;


import android.support.annotation.NonNull;

import java.util.regex.Pattern;

public class NumberUtil {

    /**
     * @param hexStr 屏幕端显示的16进制数据
     * @return 屏幕端显示的10进制数据
     */
    public static int hex2Int(@NonNull String hexStr) {
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
