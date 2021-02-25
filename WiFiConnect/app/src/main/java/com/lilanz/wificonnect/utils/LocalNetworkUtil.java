package com.lilanz.wificonnect.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 局域网络 工具类
 */
public class LocalNetworkUtil {

    /**
     * ping 指定ip地址
     *
     * @param ip
     */
    public static void pingIp(String ip) {
        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 -w 10 " + ip);
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
            dp.setAddress(InetAddress.getByName("192.168.1." + position));
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

    /**
     * 读取同一网段的所有设备ip（除当前设备ip）
     *
     * @return
     */
    public static Map<String, String> readArp() {
        Map<String, String> ipMacMap = new HashMap<>();
        try {
            Process ipProc = Runtime.getRuntime().exec("ip neighbor");
            ipProc.waitFor();
            if (ipProc.exitValue() != 0) {
                throw new Exception("Unable to access ARP entries");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(ipProc.getInputStream(), "UTF-8"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] neighborLine = line.split("\\s+");

                // We don't have a validated ARP entry for this case.
                if (neighborLine.length <= 4) {
                    continue;
                }

                String ipaddr = neighborLine[0];
                InetAddress addr = InetAddress.getByName(ipaddr);
                if (addr.isLinkLocalAddress() || addr.isLoopbackAddress()) {
                    continue;
                }

                String macAddress = neighborLine[4];
                String ip = neighborLine[0];
                ipMacMap.put(ip, macAddress);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ipMacMap;
    }
}
