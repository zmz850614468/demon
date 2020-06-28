package com.lilanz.tooldemo.utils;

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
     * 把秒数时间转换成时间格式：00:00
     *
     * @param intTime
     * @return
     */
    public static String getTimeStr(int intTime) {
        StringBuffer buffer = new StringBuffer();
        // 秒显示
        int fen = intTime / 60;
        int miao = intTime % 60;
        if (fen < 10) {
            buffer.append("0" + fen);
        } else {
            buffer.append(fen);
        }
        buffer.append(":");
        if (miao < 10) {
            buffer.append("0" + miao);
        } else {
            buffer.append(miao);
        }

        return buffer.toString();
    }

}
