package com.demon.tool.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.demon.tool.util.SerialPortUtil;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortUtils;

/**
 * 串口指令线程
 */
public class SerialOrderThread extends Thread {

    private static final int MAX_SLEET_COUNT = 10;      // 连续睡眠次数
    private static final int SLEEPING_TIME = 40;        // 每个指令执行后，睡眠时间

    // 串口 和波特率(迈冲平板)
    public static final String PORT_PATH_232_1 = "/dev/ttyS1";
    public static final int PORT_BAUDRATE_232_1 = 115200;

    // 串口 和波特率(创腾翔)
//    public static final String PORT_PATH_485_2 = "/dev/ttyS2";
//    public static final int PORT_BAUDRATE_485_2 = 115200;
    private SerialPortUtils ledSerialPort;   // led串口

    private Context context;
    private Handler handler;
    private List<String> orderList;

    private boolean canAwaken;      // 线程是否可以被叫醒
    private boolean isAnswerd;      // 串口是否已回应
    private boolean isDestroy;      // 对象是否已销毁
    private int sleetTimes;         // 线程连续睡眠次数

    public SerialOrderThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        orderList = new ArrayList<>();
        isDestroy = false;
        isAnswerd = true;

        initLedSerialPort();
    }

    private void initLedSerialPort() {
        ledSerialPort = new SerialPortUtils();
        ledSerialPort.setDataReceiveListener(portListener);
        ledSerialPort.openSerialPort(context, PORT_PATH_232_1, PORT_BAUDRATE_232_1);
    }

    OnDataReceiveListener portListener = new OnDataReceiveListener() {
        @Override
        public void onDataBack(String data) {
            isAnswerd = true;
            bean = null;

            Message msg = new Message();
            msg.what = 1;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };

    /**
     * 添加一条指令对象
     *
     * @param bean
     */
    public synchronized void addOrder(@NonNull String bean) {
        orderList.add(bean);
        if (orderList.size() == 1 && canAwaken) {
            interrupt();
        }
    }


    /**
     * 添加一系列指令对象
     *
     * @param list
     */
    public synchronized void addOrderList(@NonNull List<String> list) {
        orderList.addAll(list);
        if (!orderList.isEmpty() && orderList.size() == list.size() && canAwaken) {
            interrupt();
        }
    }

    /**
     * 获取一条指令对象
     *
     * @return
     */
    public synchronized String getOrder() {
        if (orderList.isEmpty()) {
            return null;
        }
        return orderList.remove(0);
    }


    private String bean = null;

    @Override
    public void run() {

        String laseBean = null;
        while (!isDestroy) {
            // 1. 获取下一条指令
            if (bean == null) {
                bean = getOrder();
            }

            if (bean != null) {
                // 2. 执行指令
                if (isAnswerd) {
                    isAnswerd = false;
                    // 发送指令
                    ledSerialPort.sendSerialPort(SerialPortUtil.hexStr2Byte(bean));

                    // 命令如果没被执行，是否要再次执行这条命令
                    sleetTimes = 0;
                }

                // 3. 每条命令的间隔，等待串口响应
                try {
                    sleetTimes++;
                    Thread.sleep(SLEEPING_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 4. 一条指令长时间没有响应，则视为已死(第一次没响应会在发送一次，如还没有响应则丢弃)
                if (sleetTimes == MAX_SLEET_COUNT) {
                    isAnswerd = true;
                    if (bean == laseBean) {
                        // 两次都没有响应，则丢弃数据
                        bean = null;
                    } else {
                        laseBean = bean;
                    }
                }

                // 5. 准备执行下一条指令
                continue;
            }

            try { // 没有需要执行的指令时，停止
                canAwaken = true;
                Thread.sleep(60 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            canAwaken = false;
        }
    }

    /**
     * 销毁对象资源
     */
    public void close() {
        isDestroy = true;
    }

    /**
     * 存在的命令数
     *
     * @return
     */
    public int getOrderSize() {
        return orderList.size();
    }
}
