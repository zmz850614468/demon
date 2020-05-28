package com.lilanz.tooldemo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) ? true : false;
    }

    /**
     * @return 获取系统时间格式："yyyy-MM-dd-HH-mm-ss"
     */
    public static String getDataStr() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

}
