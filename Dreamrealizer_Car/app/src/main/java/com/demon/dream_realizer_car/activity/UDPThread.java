package com.demon.dream_realizer_car.activity;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * UDP 监听类
 * 监听说有数据，并返回：received
 */
public class UDPThread extends Thread {

    private static final int LOCAL_PORT = 3344;         // 本机监听端口

    private MainActivity activity;
    private boolean isContinue;

    public UDPThread(MainActivity activity) {
        this.activity = activity;
        isContinue = true;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try {
            socket = new DatagramSocket(LOCAL_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        while (isContinue) {
            try {
                //等待客户机连接
                socket.receive(packet);
                String clientIp = packet.getAddress().getHostAddress();
                int clientPort = packet.getPort();
                String receive = new String(packet.getData(), 0, packet.getLength());
                send(socket, clientIp, clientPort, "received");
            } catch (Exception e) {
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
}
