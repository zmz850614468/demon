package com.lilanz.kotlintool.utils

import org.junit.Test

import org.junit.Assert.*

/**
 *
 */
class StringUtilTest {
    @Test
    fun emptytest() {
        println(StringUtil.isEmpty(""))
        println(StringUtil.isEmpty(null))
        println(StringUtil.isEmpty("empty"))
    }

    @Test
    fun dataStrtest() {
        println(StringUtil.getDataStr())
    }

    @Test
    fun inttest() {
        println(NumberUtil.isInt("356."))
        println(NumberUtil.isInt("256"))
        println(NumberUtil.isInt("213.5"))
    }

    @Test
    fun numbertest() {
        println(NumberUtil.isNumber("36."))
        println(NumberUtil.isNumber("21.34"))
        println(NumberUtil.isNumber("2153"))
        println(NumberUtil.isNumber("2154l"))
        println(NumberUtil.isNumber("2145s"))
    }

    @Test
    fun hextest() {
        println(NumberUtil.hex2Int("AA"))
        println(NumberUtil.hex2Int("1f"))
        println(NumberUtil.hex2Int("50"))
    }

    @Test
    fun hex2Byte() {
        var byteArray = SerialPortUtil.hexStr2Byte("01 06 a2 56 b3")
        if (byteArray != null) {
            var strArr = SerialPortUtil.byte2HexStr(byteArray)
        }
    }


}
