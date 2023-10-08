package com.demon.fit.data_transfer.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.data_transfer.func.UDPThread;
import com.demon.fit.data_transfer.func.UdpConfig;
import com.demon.fit.data_transfer.websocket.SocketConfig;
import com.demon.fit.data_transfer.websocket.WSocketServer;
import com.demon.fit.data_transfer.websocket.WSocketServerThread;
import com.demon.fit.util.WifiUtil;
import com.google.gson.Gson;

import org.java_websocket.WebSocket;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendMsgToHMActivity extends AppCompatActivity {

    @BindView(R.id.tv_today_bean_count)
    public TextView tvTodayBeanCount;
    @BindView(R.id.tv_today_bean_receiver_count)
    public TextView tvTodayBeanReceiverCount;
    @BindView(R.id.tv_result_bean_count)
    public TextView tvResultBeanCount;
    @BindView(R.id.tv_result_bean_receiver_count)
    public TextView tvResultBeanReceiverCount;

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;
    @BindView(R.id.tv_udp_service_status)
    public TextView tvUdpServiceStatus;

    private UDPThread udpReceiverThread;
    private WSocketServerThread wSocketServerThread;

    private int todayBeanTotal;
    private int resultBeanTotal;
    private int todayBeanReceiverCount;
    private int resultBeanReceiverCount;

    List<OperateTodayBean> list;    // 次数据量
    List<OperateResultBean> list1;  // 天数据量

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_data);
        ButterKnife.bind(this);

//        initUdp();
        initWifiStatus();
        initWSocketServerThread();
//        showDeleteTip();
        list = DBControl.quaryAll(this, OperateTodayBean.class);
        list1 = DBControl.quaryAll(this, OperateResultBean.class);
        tvTodayBeanReceiverCount.setText(list1.size() + "");
        tvResultBeanReceiverCount.setText(list.size() + "");
    }

    /**
     * 初始化udp服务端 和 udp发送端
     */
    private void initUdp() {
        udpReceiverThread = new UDPThread(UdpConfig.SERVER_UDP_PORT);
        udpReceiverThread.setOnUdpReceiverListener(new UDPThread.OnUdpReceiverListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> tvUdpServiceStatus.setText("Udp服务开启"));
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> tvUdpServiceStatus.setText("Udp服务关闭"));
            }

            @Override
            public void onReceiver(String ip, int port, String data) {
                showLog("接收到udp数据：" + ip + ":" + port + " -- " + data);
                if (UdpConfig.getSendData(SendMsgToHMActivity.this).equals(data)) {
                    sendUdpMsg(ip, port, UdpConfig.getReceiverAnswer(SendMsgToHMActivity.this));
                }
            }
        });
        udpReceiverThread.start();
    }

    private void sendUdpMsg(String ip, int port, String data) {
        int code = udpReceiverThread.send(ip, port, data);
        showLog("sendUdp code:" + code);
    }

    private void initWifiStatus() {
        if (!WifiUtil.checkEnableWifi(this)) {
            tvWifiStatus.setText("wifi未连接");
        } else {
            tvWifiStatus.setText(WifiUtil.getLocalIpAddress(this));
        }
    }

    private void initWSocketServerThread() {
        wSocketServerThread = new WSocketServerThread(SocketConfig.PORT, new WSocketServer.OnWebSocketServerListener() {
            @Override
            public void onWebSocketOpen(WebSocket conn) {
            }

            @Override
            public void onWebSocketClose(WebSocket conn) {
            }

            @Override
            public void onWebSocketMsg(WebSocket conn, String msgType, String msg) {
                showLog("数据包 - " + msgType + " - " + msg);
//                dealMsg(conn, msgType, msg);
                if ("hm-os".equals(msg)) {
                    sendMsgToHM(conn);
                }
            }

            @Override
            public void onServerStart(int port) {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("webSocket服务器开启");
                });
                showLog("开启webSocket服务器端口：" + port);
            }

            @Override
            public void onServerError(WebSocket conn, Exception ex) {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("webSocket服务器开启错误");
                });
                webSocketServerHandle.sendEmptyMessageDelayed(RESTART_WEB_SOCKET_SERVER, 3000);
            }
        });
        wSocketServerThread.start();
    }

    public static final int RESTART_WEB_SOCKET_SERVER = 1;
    private Handler webSocketServerHandle = new Handler(Looper.myLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == RESTART_WEB_SOCKET_SERVER) {
                wSocketServerThread.startWebSocketServer();
            }
            return true;
        }
    });


    /**
     * poundage
     * 发送数据到鸿蒙设备
     */
    private void sendMsgToHM(WebSocket conn) {

        for (OperateTodayBean bean : list) {
            conn.send(new Gson().toJson(bean));
        }

        for (OperateResultBean bean : list1) {
            conn.send(new Gson().toJson(bean));
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast("数据发送完成");
            }
        });
    }


    /**
     * 处理数据
     *
     * @param conn
     * @param msgType
     * @param msg
     */
    private void dealMsg(WebSocket conn, String msgType, String msg) {
        switch (msgType) {
            case SocketConfig.MSG_TODAY_BEAN_COUNT:
                todayBeanTotal = Integer.parseInt(msg);
                todayBeanReceiverCount = 0;
                refreshUiHandler.removeMessages(1);
                refreshUiHandler.sendEmptyMessageDelayed(1, 500);
                runOnUiThread(() -> {
                    tvTodayBeanCount.setText("每天操作结果数据量(" + msg + ")：");
                    tvTodayBeanReceiverCount.setText("0");
                });
                break;
            case SocketConfig.MSG_RESULT_BEAN_COUNT:
                resultBeanTotal = Integer.parseInt(msg);
                resultBeanReceiverCount = 0;
                refreshUiHandler.removeMessages(1);
                refreshUiHandler.sendEmptyMessageDelayed(1, 500);
                runOnUiThread(() -> {
                    tvResultBeanCount.setText("每笔操作结果数据量(" + msg + ")：");
                    tvResultBeanReceiverCount.setText("0");
                });
                break;
            case SocketConfig.MSG_TODAY_BEAN:
                todayBeanReceiverCount++;
                DBControl.createOrUpdate(this, OperateTodayBean.class, new Gson().fromJson(msg, OperateTodayBean.class));
                break;
            case SocketConfig.MSG_RESULT_BEAN:
                showLog("数据包 - " + msgType + " - " + msg);
                resultBeanReceiverCount++;

                DBControl.createOrUpdate(this, OperateResultBean.class, new Gson().fromJson(msg, OperateResultBean.class));
                break;
        }
    }

    private Handler refreshUiHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            tvTodayBeanReceiverCount.setText(todayBeanReceiverCount + "");
            tvResultBeanReceiverCount.setText(resultBeanReceiverCount + "");
            if (todayBeanTotal != todayBeanReceiverCount || resultBeanTotal != resultBeanReceiverCount) {
                refreshUiHandler.sendEmptyMessageDelayed(1, 500);
            } else {
                int count = DBControl.quaryAll(SendMsgToHMActivity.this, OperateResultBean.class).size();
                showLog("接收到数量:" + count);
                showToast("全部数据接收完成");
            }
            return true;
        }
    });

    /**
     * 如果数据库有旧，数据则提示是否删除数据
     */
    private void showDeleteTip() {
        List<OperateTodayBean> todayList = DBControl.quaryAll(this, OperateTodayBean.class);
        List<OperateResultBean> resultList = DBControl.quaryAll(this, OperateResultBean.class);

        if (!todayList.isEmpty() || !resultList.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("注意:")
                    .setMessage("是否要删除旧数据库的数据")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", (dialog, which) -> {
                        DBControl.delete(SendMsgToHMActivity.this, OperateTodayBean.class, todayList);
                        DBControl.delete(SendMsgToHMActivity.this, OperateResultBean.class, resultList);
                    });
            builder.create().show();
        }
    }

    @Override
    protected void onDestroy() {
        if (udpReceiverThread != null) {
            udpReceiverThread.close();
        }
        wSocketServerThread.onDestroy();
        super.onDestroy();
    }

    private void showLog(String msg) {
        Log.e("ReceiverDeviceActivity", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
