package com.demon.tool.data_transfer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.data_transfer.udp.UDPThread;
import com.demon.tool.data_transfer.udp.UdpConfig;
import com.demon.tool.data_transfer.websocket.SocketConfig;
import com.demon.tool.data_transfer.websocket.WSocketClient;
import com.demon.tool.data_transfer.websocket.WSocketClientThread;
import com.demon.tool.util.StringUtil;
import com.demon.tool.util.WifiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendDeviceActivity extends AppCompatActivity {

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;

    private UDPThread udpThread;
    private WSocketClientThread wSocketClientThread;
    private String serverIp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        ButterKnife.bind(this);

        initUdp();
        initWifiStatus();
    }

    private void initWSocketClientThread(String uri) {
        wSocketClientThread = new WSocketClientThread(uri, new WSocketClient.OnWebSocketClientListener() {
            @Override
            public void onOpen() {
                runOnUiThread(() -> {
                    tvWebSocketStatus.setText("连接上webSocket服务器");
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initWSocketClientThread("ws://" + ip + ":" + SocketConfig.PORT);
                            }
                        });
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
        if (StringUtil.isEmpty(serverIp) && WifiUtil.checkEnableWifi(this)) {
            showLog("查找服务器ip地址");
            String localIp = WifiUtil.getLocalIpAddress(this);
            int temp = Integer.parseInt(localIp.substring(localIp.lastIndexOf(".") + 1));
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
