package com.lilanz.kotlintool.utils

import java.text.SimpleDateFormat
import java.util.*

class StringUtil {
    companion object {

        /**
         * 判断字符串是否为空
         */
        fun isEmpty(str: String?): Boolean {
            return str == null || "" == str
        }

        /**
         * 获取当前时间  格式："yyyy-MM-dd-HH-mm-ss"
         */
        fun getDataStr(): String {
            var format = SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
            var date = Date(System.currentTimeMillis())
            return format.format(date)
        }
    }

}