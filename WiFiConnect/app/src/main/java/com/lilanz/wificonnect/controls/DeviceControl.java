package com.lilanz.wificonnect.controls;

import android.content.Context;
import android.util.Log;

import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.beans.DeviceBean;
import com.lilanz.wificonnect.beans.DeviceControlBean;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.daos.DBControl;
import com.lilanz.wificonnect.threads.OnReceiverListener;
import com.lilanz.wificonnect.threads.SocketThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 设备控制类
 */
public class DeviceControl {

    private static DeviceControl instance;

    public static DeviceControl getInstance(Context context) {
        if (instance == null) {
            synchronized (context) {
                if (instance == null) {
                    instance = new DeviceControl(context);
                }
            }
        }
        return instance;
    }

    private Context context;
    private Map<String, SocketThread> socketThreadMap = new HashMap<>();
    private Map<String, DeviceControlBean> deviceControlBeanMap = new HashMap<>();

    private DeviceControl(Context context) {
        this.context = context;
    }

    public void handleMsg(DeviceControlBean bean) {
        // 移除 使用了一定次数的socket
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, SocketThread> entry : socketThreadMap.entrySet()) {
            if (entry.getValue().getSendMsgTimes() >= 3) {
                list.add(entry.getKey());
            }
        }
        for (String s : list) {
            SocketThread thread = socketThreadMap.remove(s);
            if (thread != null) {
                thread.close();
            }
        }


        if (!socketThreadMap.containsKey(bean.ip)) {
            openSocket(bean.ip, bean.port);
            deviceControlBeanMap.put(bean.ip, bean);
        } else {
            SocketThread socketThread = socketThreadMap.get(bean.ip);
            socketThread.sendMsg(bean.control.getBytes());
//            autoCloseAfterOpen(socketThread, 100);
        }
    }

    /**
     * 开启WiFi通信
     *
     * @param ip
     * @param port
     */
    private void openSocket(String ip, int port) {
        SocketThread socketThread = new SocketThread();

        socketThread.setListener(new OnReceiverListener() {
            @Override
            public void onReceiver(String msg) {
                super.onReceiver(msg);
                showLog(msg);
                if ("open".equals(msg) || "close".equals(msg)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("ip", ip);
                    map.put("port", port);
                    List<DeviceBean> list = DBControl.quaryByColumn(context, DeviceBean.class, map);
                    if (!list.isEmpty()) {
                        sendMsg(list.get(0), msg);
                    }
                }
            }

            /**
             * 网络 发送设备状态信息
             * @param deviceBean
             * @param status
             */
            private void sendMsg(DeviceBean deviceBean, String status) {
                deviceBean.status = status;
                MsgBean msgBean = new MsgBean(MsgBean.DEVICE_STATUS_BALLBACK, deviceBean.toString());
                AppDataControl.sendMsg(msgBean);
            }

            @Override
            public void onConnected(SocketThread thread, String msg) {
                super.onConnected(thread, msg);
                DeviceControlBean bean = deviceControlBeanMap.remove(thread.getIp());
                if (bean != null) {
                    thread.sendMsg(bean.control.getBytes());
//                    autoCloseAfterOpen(thread, 100);
                }
            }

            @Override
            public void onDisconnect(SocketThread thread, String msg) {
                super.onDisconnect(thread, msg);
                removeSocket(thread);
            }

            @Override
            public void onError(SocketThread thread, String msg) {
                super.onError(thread, msg);
                showLog("onError: " + msg);
                removeSocket(thread);
            }
        });
        socketThread.startConnect(ip, port);
        addSocket(ip, socketThread);
    }

    /**
     * 自动发送数据后，自动关闭
     *
     * @param thread
     * @param delay
     */
    private void autoCloseAfterOpen(SocketThread thread, long delay) {
        if (thread != null) {
            thread.sendMsg("open".getBytes());
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (thread != null) {
                        thread.sendMsg("close".getBytes());
                    }
                }
            }, delay);
        }
    }

    private void addSocket(String ip, SocketThread socketThread) {
        socketThreadMap.put(ip, socketThread);
    }

    private void removeSocket(SocketThread socketThread) {
        if (socketThreadMap.containsKey(socketThread.getIp())) {
            socketThreadMap.remove(socketThread.getIp());
        }
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("DeviceControl", msg);
        }
    }

}
