package com.lilanz.tooldemo.multiplex.wificonnect;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiUtil {

    /**
     * 根据Wifi信息获 本机ip地址
     *
     * @param context
     * @return
     */
    public static String getIpAddr(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return ipInt2String(info.getIpAddress());
    }

    /**
     * int型 ip地址转 String型ip地址
     *
     * @param intIp
     * @return
     */
    public static String ipInt2String(int intIp) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf((intIp & 0x000000FF))).append(".");
        builder.append(String.valueOf((intIp & 0x0000FFFF) >> 8) + ".");
        builder.append(String.valueOf((intIp & 0x00FFFFFF) >> 16) + ".");
        builder.append(String.valueOf((intIp >> 24) & 0x000000FF));
        return builder.toString();
    }

}
