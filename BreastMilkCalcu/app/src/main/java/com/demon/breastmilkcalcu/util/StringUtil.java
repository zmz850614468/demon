package com.demon.breastmilkcalcu.util;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * @return 获取系统时间格式："yyyy-MM-dd"
     */
    public static String getDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd"
     */
    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd"
     */
    public static String getDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDay(calendar.getTime());
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd"
     */
    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDate(calendar.getTime());
    }

    /**
     * 返回时间戳
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long getTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 简单判断字符串是否完整
     *
     * @param content
     * @return
     */
    public static int isJsonComplete(@NonNull String content) {
        int count = 0;
        char ch;
        for (int i = 0; i < content.length(); i++) {
            ch = content.charAt(i);
            if (ch == '{') {
                count++;
            } else if (ch == '}') {
                count--;
            }

            if (count == 0) {
                return i;
            }
        }

        return 0;
    }

}
