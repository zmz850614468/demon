package com.demon.opencvbase.util;

public class SerialPortUtil {

    /**
     * @param buffer 串口传回来的16进制数据
     * @return 屏幕能够显示的16进制数据
     */
    public static String byte2hexStr(byte[] buffer) {
        String h = "";
        for (int i = 0; i < buffer.length; i++) {
            String temp = Integer.toHexString(buffer[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            h = h + temp;
        }

        return h;
    }

    /**
     * @param inputStr 屏幕端显示的16进制字符串
     * @return 机器能够识别的16进制字节（可以直接发给串口使用）
     */
    public static byte[] hexStr2Byte(String inputStr) {
        if (inputStr == null || inputStr.equals("")) {
            return null;
        }
        inputStr = inputStr.replace(" ", "");
        byte[] baKeyword = new byte[inputStr.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(inputStr.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return baKeyword;
    }

    /**
     * 垂直校验
     *
     * @param str
     * @return
     */
    public static String verticalCheck(String str) {
        str = str.replace(" ", "");
        if (str.length() % 2 != 0) {
            return "error";
        }

        int count = 0;
        for (int i = 0; i < str.length(); i = i + 2) {
            count += NumberUtil.hex2Int(str.substring(i, i + 2));
        }

        int result = count % 256;

        return String.format("%02x", result);
    }

}
