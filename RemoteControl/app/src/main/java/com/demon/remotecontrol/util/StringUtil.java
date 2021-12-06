package com.demon.remotecontrol.util;

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
     * @return 获取系统时间格式："yy-MM-dd"
     */
    public static String getDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
//        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @return 获取系统时间格式："yy-MM-dd"
     */
    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
//        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * @return 获取系统时间格式："yy-MM-dd"
     */
    public static String getDay(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDay(calendar.getTime());
    }

    /**
     * @return 获取系统时间格式："yy-MM-dd"
     */
    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return getDate(calendar.getTime());
    }
}
