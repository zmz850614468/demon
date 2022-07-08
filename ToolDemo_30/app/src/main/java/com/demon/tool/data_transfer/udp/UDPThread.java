package com.demon.tool.data_transfer.udp;

import android.util.Log;

import com.demon.tool.util.StringUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * UDP 监听 和 发送类
 */
public class UDPThread extends Thread {


    private boolean isContinue;
    private DatagramSocket socket;
    private int port;

    public UDPThread(int port) {
        isContinue = true;
        this.port = port;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            showLog("打开UDP服务异常:" + e.getMessage());
            return;
        }

        if (onUdpReceiverListener != null) {
            onUdpReceiverListener.onConnected();
        }
        showLog("打开UDP服务:" + port);
        while (isContinue) {
            try {
                //等待客户机连接
                socket.receive(packet);
                String clientIp = packet.getAddress().getHostAddress();
                int clientPort = packet.getPort();
                String receive = new String(packet.getData(), 0, packet.getLength(), "UTF-8");

                if (!StringUtil.isEmpty(receive) && onUdpReceiverListener != null) {
                    onUdpReceiverListener.onReceiver(clientIp, clientPort, receive);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showLog("UDP服务等待连接异常：" + e.getMessage());
            }
        }

        if (onUdpReceiverListener != null) {
            onUdpReceiverListener.onDisconnected();
        }
    }

    /**
     * 发送udp信息
     *
     * @param ip
     * @param port
     * @param data
     */
    public int send(String ip, int port, String data) {
        if (socket == null) {
            return -1;
        }
        try {
            byte[] bytes = data.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
            packet.setAddress(InetAddress.getByName(ip));
            packet.setPort(port);
            socket.send(packet);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
            showLog("发送udp数据异常-3：" + e.getMessage());
            return -3;
        }
        return 0;
    }

    private void showLog(String msg) {
        Log.e("udp", msg);
    }

    public void close() {
        isContinue = false;
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    public boolean isUdpConnected() {
        if (socket == null) {
            return false;
        } else {
            return socket.isConnected();
        }
    }

    private OnUdpReceiverListener onUdpReceiverListener;

    public void setOnUdpReceiverListener(OnUdpReceiverListener onUdpReceiverListener) {
        this.onUdpReceiverListener = onUdpReceiverListener;
    }

    public interface OnUdpReceiverListener {
        void onConnected();

        void onDisconnected();

        void onReceiver(String ip, int port, String data);
    }
}
