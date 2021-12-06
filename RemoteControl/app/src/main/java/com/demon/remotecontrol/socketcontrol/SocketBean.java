package com.demon.remotecontrol.socketcontrol;

/**
 * socket 通信类
 */
public class SocketBean {

    public static final String DEVICE_SERVER = "server";
    /**
     * server : 服务器需要处理的消息
     * 其他编号 : 其他设备编号都是服务器给的
     */
    public String from; // 消息来源（设备编号）
    public String to;   // 消息去处（设备编号）


    public static final int SOCKET_TYPE_QUERY_ALL_DEVICE = 2;  // 查询所有设备编号
    public static final int SOCKET_TYPE_RETRANSMISSION = 4;    // 转发消息
    public int command;         // 消息类型

    public static final int SOCKET_TYPE_NOT_FIND_DEVICE = -1;  // 没找到对应设备编号
    public static final int SOCKET_TYPE_REPEAT = 0;            // 消息转发
//    public static final int SOCKET_TYPE_DEVICE_ID = 1;         // 分配设备编号
    public static final int SOCKET_TYPE_ALL_DEVICE = 3;        // 所有设备编号
    public int status;          // 服务器返回的状态值

    public String msg;         // 具体消息内容

}
