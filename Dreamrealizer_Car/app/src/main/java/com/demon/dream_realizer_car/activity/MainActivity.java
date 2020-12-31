package com.demon.dream_realizer_car.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.demon.dream_realizer_car.R;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String ip = "192.168.4.1";
    public static final int port = 81;

    private IConnectionManager socketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (socketManager == null) {
            connectSocket(ip, port);
        }
    }

    private void connectSocket(String ip, int port) {
        socketManager = OkSocket.open(ip, port);
        socketManager.registerReceiver(socketActionAdapter);
        socketManager.connect();
    }

    private SocketActionAdapter socketActionAdapter = new SocketActionAdapter() {
        @Override
        public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
            super.onSocketConnectionSuccess(info, action);
            showLog(info.getIp() + ":连接成功");
        }

        @Override
        public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
            super.onSocketDisconnection(info, action, e);
            socketManager = null;
            showLog(info.getIp() + ":断开连接");
        }

        @Override
        public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
            super.onSocketConnectionFailed(info, action, e);
            showLog(info.getIp() + ":连接失败");
            if (socketManager != null) {
                socketManager.disconnect();
            }
        }
    };

    private void initUI() {
        ;
    }

    private void showLog(String msg) {
//        Log.e("", msg);
        showToast(msg);
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


