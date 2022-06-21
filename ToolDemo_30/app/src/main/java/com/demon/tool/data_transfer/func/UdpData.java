package com.demon.tool.data_transfer.func;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class UdpData {

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
