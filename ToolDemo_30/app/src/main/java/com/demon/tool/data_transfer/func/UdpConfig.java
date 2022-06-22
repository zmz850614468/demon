package com.demon.tool.data_transfer.func;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class UdpConfig {

    public static final int SERVER_UDP_PORT = 18899;         // udp听端口
    public static final int CLIENT_UDP_PORT = 17788;         // udp听端口

    // UDP发送方数据
    private static final String UDP_SEND_DATA = "udpSendData";
    // UDP接收方应答数据
    private static final String UDP_RECEIVER_ANSWER = "udpReceiverAnswer";

    public static String getSendData(Context context) {
        return context.getPackageName() + "_" + UDP_SEND_DATA;
    }

    public static String getReceiverAnswer(Context context) {
        return context.getPackageName() + "_" + UDP_RECEIVER_ANSWER;
    }
    
}
