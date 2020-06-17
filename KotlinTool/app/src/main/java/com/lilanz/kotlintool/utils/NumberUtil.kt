package com.lilanz.kotlintool.utils

import java.lang.Exception

class NumberUtil {
    companion object {
        /**
         * 判断字符串是不是整数
         */
        fun isInt(str: String): Boolean {
            return try {
                str.toInt()
                true
            } catch (e: Exception) {
                false
            }
        }

        /**
         * 判断字符串是不是整数
         */
        fun isNumber(str: String): Boolean {
            return try {
                str.toDouble()
                true
            } catch (e: Exception) {
                false
            }
        }

        /**
         * 十六进制字符串转十进制数值
         */
        fun hex2Int(str: String): Int {
            if (str == "") {
                return 0
            }

            var count = 0;
            var num = 0
            for (c in str.toCharArray()) {
                num = when (c) {
                    'a', 'A' -> 10
                    'b', 'B' -> 11
                    'c', 'C' -> 12
                    'd', 'D' -> 13
                    'e', 'E' -> 14
                    'f', 'F' -> 15
                    else -> c - '0'
                }
                count = count * 16 + num
            }

            return count
        }
    }
}