package com.lilanz.tooldemo.utils;


import android.support.annotation.NonNull;

import java.util.regex.Pattern;

public class NumberUtil {

    /**
     * 正则表达式说明:
     * [\+-]?  + -号可出现也可不出现
     * [0-9]*  整数部分是否出现    [0-9]可以用\\d代替
     * (\.[0-9])?  出现小数点后面必须跟数字
     * ([eE][\+-]?[0-9]+)  若有指数部分E或e肯定出现 + -可以不出现
     * 紧接着可以跟着整数，也可以什么都没有
     */

    /**
     * 判断是否为整数
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是数值
      * @param str
     * @return
     */
    public static boolean isNumerber(String str) {
        //1  用正则表达式   判断其是否是数字
        return str.matches("[\\+-]?[0-9]*(\\.[0-9])?([eE][\\+-]?[0-9]+)?");
    }

    /**
     * @param hexStr 屏幕端显示的16进制数据
     * @return 屏幕端显示的10进制数据
     */
    public static int hexStr2Int(@NonNull String hexStr) {
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
