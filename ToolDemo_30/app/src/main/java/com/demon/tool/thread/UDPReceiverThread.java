package com.demon.tool.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.demon.tool.util.StringUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * UDP 监听类
 */
public class UDPReceiverThread extends Thread {

    private static final int LOCAL_PORT = 18899;         // 本机监听端口

    private boolean isContinue;
    private Handler handler;
    private DatagramSocket socket;

    public UDPReceiverThread(Handler handler) {
        this.handler = handler;
        isContinue = true;
    }

    @Override
    public void run() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try {
            socket = new DatagramSocket(LOCAL_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        showLog("打开UDP服务:" + LOCAL_PORT);
        while (isContinue) {
            try {
                //等待客户机连接
                socket.receive(packet);
//                String clientIp = packet.getAddress().getHostAddress();
//                int clientPort = packet.getPort();
//                send(socket, clientIp, clientPort, "received");
                String receive = new String(packet.getData(), 0, packet.getLength());

                if (!StringUtil.isEmpty(receive)) {
                    Message msg = new Message();
                    msg.what = 200;
                    msg.obj = receive;
                    handler.sendMessage(msg);
                }
//                showLog("udp msg :" + receive);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (socket != null) {
            socket.close();
            socket = null;
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
