package com.lilanz.wificonnect.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PingUtil {

    /**
     * ping 指定ip地址
     *
     * @param ip
     */
    public static void pingIp(String ip) {
        try {
            java.lang.Process process = Runtime.getRuntime().exec("ping -c 1 -w 10 " + ip);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送udp包给路由器网段
     *
     * @throws Exception
     */
    public static void sendUdpMsg() throws Exception {
        DatagramPacket dp = new DatagramPacket(new byte[0], 0, 0);
        DatagramSocket socket = new DatagramSocket();
        int position = 2;
        while (position < 255) {
            dp.setAddress(InetAddress.getByName("192.168.1." + String.valueOf(position)));
            socket.send(dp);
            position++;
            if (position == 125) {//分两段掉包，一次性发的话，达到236左右，会耗时3秒左右再往下发
                socket.close();
                socket = new DatagramSocket();
            }
        }
        socket.close();
    }

    /**
     * 发送udp包给路由器网段
     *
     * @throws Exception
     */
    public static void sendUdpMsg(String ip, byte[] bytes) throws Exception {
        DatagramPacket dp = new DatagramPacket(bytes, 0, bytes.length);
        DatagramSocket socket = new DatagramSocket();
        dp.setAddress(InetAddress.getByName(ip));
        dp.setPort(8080);
        socket.send(dp);
        socket.close();
    }
}
