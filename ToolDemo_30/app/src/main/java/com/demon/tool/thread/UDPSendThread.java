package com.demon.tool.thread;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 间断性向服务器发送UDP信息，保存服务器知道当前设备
 */
public class UDPSendThread extends Thread {

    private static final int LOCAL_PORT = 14455;         // 本机监听端口
//    private static final String HOST_IP = "192.168.35.137";
//    private static final int HOST_PORT = 16000;

    private boolean isContinue;
    private List<UdpBean> udpList = new ArrayList<>();

    public UDPSendThread() {
        isContinue = true;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(LOCAL_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (isContinue) {
            synchronized (this) {
                for (UdpBean bean : udpList) {
                    send(socket, bean.ip, bean.port, bean.msg);
                }
                udpList.clear();
            }

            try {
                Thread.sleep(10000);
//                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    /**
     * 发送数据
     *
     * @param socket
     * @param ip
     * @param port
     * @param msg
     */
    private void send(DatagramSocket socket, String ip, int port, String msg) {
        try {
            DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length);
            packet.setAddress(InetAddress.getByName(ip));
            packet.setPort(port);
            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showLog(String msg) {
        Log.e("udp", msg);
    }

    public void close() {
        isContinue = true;
        interrupt();
    }

    public synchronized void addUdpMsg(String ip, int port, String msg) {
        UdpBean bean = new UdpBean(ip, port, msg);
        udpList.add(bean);
        if (udpList.size() == 1) {
            interrupt();
        }
    }

    private class UdpBean {
        public String ip;
        public int port;
        public String msg;

        public UdpBean(String ip, int port, String msg) {
            this.ip = ip;
            this.port = port;
            this.msg = msg;
        }
    }

}
