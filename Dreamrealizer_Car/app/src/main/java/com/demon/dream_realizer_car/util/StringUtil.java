package com.demon.dream_realizer_car.util;


import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd HH:mm:ss"
     */
    public static String getDataStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd"
     */
    public static String getDay() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 把秒数时间转换成时间格式：00:00
     *
     * @param intTime
     * @return
     */
    public static String getTimeStr(int intTime) {
        // 秒显示
        int fen = intTime / 60;
        int miao = intTime % 60;

        return String.format("%02d:%02d", fen, miao);
    }

}
