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
import com.demon.tool.data_transfer.func.UDPThread;
import com.demon.tool.data_transfer.func.UdpData;
import com.demon.tool.data_transfer.websocket.SocketData;
import com.demon.tool.data_transfer.websocket.WSocketServer;
import com.demon.tool.util.WifiUtil;

import org.java_websocket.WebSocket;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiverDeviceActivity extends AppCompatActivity {

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;
    @BindView(R.id.tv_udp_service_status)
    public TextView tvUdpServiceStatus;

    private UDPThread udpReceiverThread;

    private WSocketServer wSocketServer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_data);
        ButterKnife.bind(this);

        initUdp();
        initWifiStatus();
        startWebSocketServer();
    }

    /**
     * 初始化udp服务端 和 udp发送端
     */
    private void initUdp() {
        udpReceiverThread = new UDPThread();
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
                if (UdpData.getSendData(ReceiverDeviceActivity.this).equals(data)) {
                    sendUdpMsg(ip, port, UdpData.getReceiverAnswer(ReceiverDeviceActivity.this));
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

    /**
     * 开启 webSocketServer服务
     */
    private void startWebSocketServer() {
        if (wSocketServer != null) {
            try {
                wSocketServer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wSocketServer = null;
        }

        wSocketServer = new WSocketServer(SocketData.PORT);
        wSocketServer.setOnWebSocketServerListener(new WSocketServer.OnWebSocketServerListener() {
            @Override
            public void onWebSocketOpen(WebSocket conn) {
            }

            @Override
            public void onWebSocketClose(WebSocket conn) {
            }

            int index = 0;

            @Override
            public void onWebSocketMsg(WebSocket conn, String msg) {
                showLog("数据包 - " + ++index + "：" + msg);
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
        wSocketServer.start();
    }

    public static final int RESTART_WEB_SOCKET_SERVER = 1;
    private Handler webSocketServerHandle = new Handler(Looper.myLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == RESTART_WEB_SOCKET_SERVER) {
                startWebSocketServer();
            }
            return true;
        }
    });

    @Override
    protected void onDestroy() {
        udpReceiverThread.close();
        if (wSocketServer != null) {
            try {
                wSocketServer.stop();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void showLog(String msg) {
        Log.e("ReceiverDeviceActivity", msg);
    }
}
