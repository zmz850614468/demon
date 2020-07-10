package com.lilanz.wificonnect.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class WiFiUtil {

//    public static String getMacAddre(Context context) {
//        String mac_s= "";
//        StringBuilder buf = new StringBuilder();
//        try {
//            byte[] mac;
//            NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getIpAddr(context)));
//            mac = ne.getHardwareAddress();
//            for (byte b : mac) {
//                buf.append(String.format("%02X:", b));
//            }
//            if (buf.length() > 0) {
//                buf.deleteCharAt(buf.length() - 1);
//            }
//            mac_s = buf.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return mac_s;
//    }


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
        builder.append(String.valueOf(intIp >> 24));
        return builder.toString();
    }

}
