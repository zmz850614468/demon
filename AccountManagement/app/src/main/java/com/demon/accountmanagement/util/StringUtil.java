package com.demon.accountmanagement.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * 获取url带的参数
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParam(String url) {
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(url) && url.contains("?")) {
            int startIndex = url.indexOf("?");
            if (url.length() > startIndex + 1) {
                String paramStr = url.substring(startIndex + 1);
                paramStr = paramStr.replace("=", "&");
                String[] strArr = paramStr.split("&");
                for (int i = 0; i + 1 < strArr.length; i = i + 2) {
                    map.put(strArr[i], strArr[i + 1]);
                }
            }
        }

        return map;
    }

    /**
     * 对象转字节数组
     */
    public static byte[] objectToBytes(Object obj) throws IOException {
        try(
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream sOut = new ObjectOutputStream(out);
        ){
            sOut.writeObject(obj);
            sOut.flush();
            byte[] bytes = out.toByteArray();
            return bytes;
        }
    }
}
