package com.demon.tool.data_transfer.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.data_transfer.websocket.WSocketClient;
import com.demon.tool.util.WifiUtil;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendDeviceActivity extends AppCompatActivity {

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;

    private WSocketClient wSocketClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        ButterKnife.bind(this);

        initWifiStatus();
        connectWebSocket("ws://192.168.1.134:17788");
    }

    private void connectWebSocket(String uri) {
        wSocketClient = new WSocketClient(URI.create(uri));
        wSocketClient.setOnWebSocketClientListener(new WSocketClient.OnWebSocketClientListener() {
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
            }

            @Override
            public void onMessage(String msg) {

            }
        });
        wSocketClient.connect();
    }

    private void initWifiStatus() {
        if (!WifiUtil.checkEnableWifi(this)) {
            tvWifiStatus.setText("wifi未连接");
        } else {
            tvWifiStatus.setText(WifiUtil.getLocalIpAddress(this));
        }
    }

    @Override
    protected void onDestroy() {
        if (wSocketClient != null && wSocketClient.isOpen()) {
            wSocketClient.onDestroy();
            wSocketClient = null;
        }
        super.onDestroy();
    }
}
