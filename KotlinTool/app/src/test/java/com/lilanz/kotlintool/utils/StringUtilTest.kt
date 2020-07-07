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

    @Test
    fun test() {
        var list = ArrayList<String>()
        list.add("晋江")
        list.add("厦门")
        list.add("福田")
        list.add("莆田")

        for ((index, value) in list.withIndex()) {
            var a = 0
        }
    }

    @Test
    fun test2() {
        var p = Person("恶魔", 3, "eat")
        var s = p.hobbi
        p.like()
        var h = p.hobbi
        var d = p.name
        var a = p.age
        var o = 0;
    }

    data class Person(var name: String, var age: Int) {
        lateinit var hobbi: String

        constructor(name: String, age: Int, hobbi: String) : this(name, age) {
            this.hobbi = hobbi
        }

        fun like(){
            hobbi = "like"
        }
    }

}
