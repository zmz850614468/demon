package com.demon.fit.data_transfer.websocket;

public class SocketConfig {

    public static final int PORT = 16677;

    /**
     * 传输的消息类型
     * todo 可以添加新的消息类型
     */
//    public static final String MSG_TYPE = "msgType";
    public static final String MSG_TODAY_BEAN_COUNT = "todayBeanCount"; // 数据量
    public static final String MSG_TODAY_BEAN = "todayBean";    // 具体数据信息
    public static final String MSG_RESULT_BEAN_COUNT = "resultBeanCount";// 数据量
    public static final String MSG_RESULT_BEAN = "resultBean";  // 具体数据信息
}
