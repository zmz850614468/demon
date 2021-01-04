package com.demon.dream_realizer_car.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.demon.dream_realizer_car.R;
import com.demon.dream_realizer_car.bean.SocketBean;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

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

    int upOrDown = 0;       // 1:up ; 0:stop ; -1:down
    int leftOrRight = 0;    // 1:left ; 0:stop ; -1:right

    @OnTouch({R.id.bt_left, R.id.bt_right, R.id.bt_up, R.id.bt_down})
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        // 上下 和 左右 键，只能按其中一个
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.bt_left:
                    if (leftOrRight == -1) {
                        return true;
                    }
                    break;
                case R.id.bt_right:
                    if (leftOrRight == 1) {
                    return true;
            }
                    break;
                case R.id.bt_up:
                    if (upOrDown == -1) {
                        return true;
                    }
                    break;
                case R.id.bt_down:
                    if (upOrDown == 1) {
                        return true;
                    }
                    break;
            }
        }

        if (action == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.bt_left:
                    leftOrRight = 1;
                    fly("left\n\r");
                    break;
                case R.id.bt_right:
                    leftOrRight = -1;
                    fly("right\n\r");
                    break;
                case R.id.bt_up:
                    upOrDown = 1;
                    fly("up\n\r");
                    break;
                case R.id.bt_down:
                    upOrDown = -1;
                    fly("down\n\r");
                    break;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.bt_left:
                case R.id.bt_right:
                    leftOrRight = 0;
                    fly("hStop\n\r");
                    break;
                case R.id.bt_up:
                case R.id.bt_down:
                    upOrDown = 0;
                    fly("vStop\n\r");
                    break;
            }
        }
        return true;
    }

    /**
     * 奔跑吧，小车
     *
     * @param order
     */
    private void fly(String order) {
        showLog(order);
        if (socketManager != null) {
            socketManager.send(new SocketBean(order));
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
//
//        @Override
//        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
//            super.onSocketWriteResponse(info, action, data);
//            showLog(info.getIp() + ":写数据成功");
//        }
//
//        @Override
//        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
//            super.onSocketReadResponse(info, action, data);
//            showLog(info.getIp() + ":读数据成功");
//        }
    };

    private void initUI() {
    }

    private void showLog(String msg) {
        Log.e("socket", msg);
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


