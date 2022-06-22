package com.demon.fit.data_transfer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.demon.fit.R;
import com.demon.fit.bean.OperateResultBean;
import com.demon.fit.bean.OperateTodayBean;
import com.demon.fit.daos.DBControl;
import com.demon.fit.data_transfer.func.UDPThread;
import com.demon.fit.data_transfer.func.UdpConfig;
import com.demon.fit.data_transfer.websocket.SocketConfig;
import com.demon.fit.data_transfer.websocket.WSocketClient;
import com.demon.fit.data_transfer.websocket.WSocketClientThread;
import com.demon.fit.util.WifiUtil;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendDeviceActivity extends AppCompatActivity {

    @BindView(R.id.tv_data_info)
    public TextView tvDataInfo;
    @BindView(R.id.bt_send_data)
    public Button btSendData;

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;

    private UDPThread udpThread;
    private WSocketClientThread wSocketClientThread;

    private List<OperateTodayBean> todayList;
    private List<OperateResultBean> resultList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        ButterKnife.bind(this);

        initUdp();
        initWifiStatus();
        initData();
    }

    private void initWSocketClientThread(String uri) {
        wSocketClientThread = new WSocketClientThread(uri, new WSocketClient.OnWebSocketClientListener() {
            @Override
            public void onOpen() {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("连接上webSocket服务器");
                    btSendData.setVisibility(View.VISIBLE);
                    wSocketClientThread.addData(SocketConfig.MSG_TODAY_BEAN_COUNT, todayList.size() + "");
                    wSocketClientThread.addData(SocketConfig.MSG_RESULT_BEAN_COUNT, resultList.size() + "");
                });
            }

            @Override
            public void onClose() {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("断开webSocket服务器连接");
                });
            }

            @Override
            public void onError(Exception ex) {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("webSocket服务器连接错误");
                });
                wSocketClientThread.connectWebSocket();
            }

            @Override
            public void onMessage(String msgType, String msg) {
                // todo 处理接收到的数据
            }
        });
        wSocketClientThread.start();
    }

    private void initWifiStatus() {
        if (!WifiUtil.checkEnableWifi(this)) {
            tvWifiStatus.setText("wifi未连接");
        } else {
            tvWifiStatus.setText(WifiUtil.getLocalIpAddress(this));
        }
    }

    /**
     * 初始化udp服务端 和 udp发送端
     */
    private void initUdp() {
        udpThread = new UDPThread(UdpConfig.CLIENT_UDP_PORT);
        udpThread.setOnUdpReceiverListener(new UDPThread.OnUdpReceiverListener() {
            @Override
            public void onConnected() {
                findServerHandler.removeMessages(1);
                findServerHandler.sendEmptyMessage(1);
            }

            @Override
            public void onDisconnected() {
            }

            @Override
            public void onReceiver(String ip, int port, String data) {
                showLog("接收到udp数据：" + ip + ":" + port + " -- " + data);
                if (UdpConfig.getReceiverAnswer(SendDeviceActivity.this).equals(data)) {
                    showLog("找到服务ip地址：" + ip);
                    findServerHandler.removeMessages(1);
                    // todo
                    if (wSocketClientThread == null) {
                        runOnUiThread(() -> initWSocketClientThread("ws://" + ip + ":" + SocketConfig.PORT));
                    }
                }
            }
        });
        udpThread.start();
    }

    /**
     * 查找服务器ip地址
     */
    private void findServer() {
        if (WifiUtil.checkEnableWifi(this)) {
            showLog("查找服务器ip地址");
            String localIp = WifiUtil.getLocalIpAddress(this);
            localIp = localIp.substring(0, localIp.lastIndexOf(".") + 1);

            String finalLocalIp = localIp;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String msg = UdpConfig.getSendData(SendDeviceActivity.this);
                    udpThread.send(finalLocalIp + "0", UdpConfig.SERVER_UDP_PORT, msg);
                }
            }.start();
        }
    }

    private Handler findServerHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            findServer();
            findServerHandler.sendEmptyMessageDelayed(1, 3000);
            return true;
        }
    });

    private void initData() {
        todayList = DBControl.quaryAll(this, OperateTodayBean.class);
        resultList = DBControl.quaryAll(this, OperateResultBean.class);

        tvDataInfo.setText("每天操作结果数据量：" + todayList.size() + "\n" +
                "每笔操作结果数据量：" + resultList.size());
    }

    @OnClick(R.id.bt_send_data)
    public void onSendDataClicked(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("注意:")
                .setMessage("确定要开始传输数据吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", (dialog, which) -> {
                    for (OperateTodayBean todayBean : todayList) {
                        wSocketClientThread.addData(SocketConfig.MSG_TODAY_BEAN, new Gson().toJson(todayBean));
                    }
                    for (OperateResultBean resultBean : resultList) {
                        wSocketClientThread.addData(SocketConfig.MSG_RESULT_BEAN, new Gson().toJson(resultBean));
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        if (wSocketClientThread != null) {
            wSocketClientThread.onDestroy();
        }
        udpThread.close();
        super.onDestroy();
    }

    private void showLog(String msg) {
        Log.e("SendDeviceActivity", msg);
    }
}
