package com.lilanz.kotlintool.utils

import kotlin.experimental.and

/**
 * 都是机器数据与屏幕显示数据间的转换
 */
class SerialPortUtil {
    companion object {
        /**
         * 串口16进制数据 转 屏幕显示16进制数据
         */
        fun byte2HexStr(buffer: ByteArray): String {
            var str = ""
            for (byte in buffer) {
                var temp = Integer.toHexString((byte and 0xFF.toByte()).toInt())
                if (temp.length == 1) {
                    temp = "0" + temp
                } else if (temp.length > 2) {
                    temp = temp.substring(temp.length - 2, temp.length)
                }
                str += temp
            }
            return str
        }

        /**
         * 16进制屏幕数据 转 16进制串口数据
         */
        fun hexStr2Byte(str: String?): ByteArray? {
            if (str != null && !"".equals(str)) {
                var temp = str.replace(" ", "")
                var byteArray = ByteArray(temp.length / 2)

                for (i in 0 until byteArray.size) {
                    byteArray[i] = (0xff and temp.substring(i * 2, i * 2 + 2).toInt(16)).toByte()
                }
                return byteArray
            }
            return null
        }
    }
}