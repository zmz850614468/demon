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
import com.demon.tool.data_transfer.websocket.WSocketClient;
import com.demon.tool.data_transfer.websocket.WSocketClientThread;
import com.demon.tool.util.WifiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendDeviceActivity extends AppCompatActivity {

    @BindView(R.id.tv_wifi_status)
    public TextView tvWifiStatus;
    @BindView(R.id.tv_web_socket_status)
    public TextView tvWebSocketStatus;

    private WSocketClientThread wSocketClientThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        ButterKnife.bind(this);

        initWifiStatus();
        initWSocketClientThread("ws://192.168.1.134:17788");
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
            }

            @Override
            public void onMessage(String msg) {

            }
        });
        wSocketClientThread.start();
    }

//    private Handler testHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//
//            for (int i = 1; i <= 1000; i++) {
//                wSocketClientThread.addData("数据库数据 -- " + i);
//            }
//
//            return true;
//        }
//    });

    private void initWifiStatus() {
        if (!WifiUtil.checkEnableWifi(this)) {
            tvWifiStatus.setText("wifi未连接");
        } else {
            tvWifiStatus.setText(WifiUtil.getLocalIpAddress(this));
        }
    }

    @Override
    protected void onDestroy() {
        wSocketClientThread.onDestroy();
        super.onDestroy();
    }

    private void showLog(String msg) {
        Log.e("SendDeviceActivity", msg);
    }
}
