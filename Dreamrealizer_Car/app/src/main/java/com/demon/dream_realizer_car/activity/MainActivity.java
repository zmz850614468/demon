package com.demon.dream_realizer_car.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.demon.dream_realizer_car.R;
import com.demon.dream_realizer_car.bean.SocketBean;
import com.demon.dream_realizer_car.bean.TravelBean;
import com.demon.dream_realizer_car.util.StringUtil;
import com.demon.dream_realizer_car.view.TravelView;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

    public static final String ip = "192.168.4.1";
    public static final int port = 81;

    private IConnectionManager socketManager;
    private UDPThread udpThread;

    @BindView(R.id.travel_view)
    TravelView travelView;

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

    @OnCheckedChanged(R.id.cb_travel)
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            travelView.setVisibility(View.VISIBLE);
        } else {
            travelView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tv_reset)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_reset:
                travelView.reset();
                break;
        }
    }

    int upOrDown = 0;       // 1:up ; 0:stop ; -1:down
    int leftOrRight = 0;    // 1:left ; 0:stop ; -1:right

    @OnTouch({R.id.tv_left, R.id.tv_right, R.id.tv_up, R.id.tv_down})
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        // 上下 和 左右 键，只能按其中一个
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.tv_left:
                    if (leftOrRight == -1) {
                        return true;
                    }
                    break;
                case R.id.tv_right:
                    if (leftOrRight == 1) {
                        return true;
                    }
                    break;
                case R.id.tv_up:
                    if (upOrDown == -1) {
                        return true;
                    }
                    break;
                case R.id.tv_down:
                    if (upOrDown == 1) {
                        return true;
                    }
                    break;
            }
        }

        if (action == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.tv_left:
                    leftOrRight = 1;
                    fly("left\n\r");
                    break;
                case R.id.tv_right:
                    leftOrRight = -1;
                    fly("right\n\r");
                    break;
                case R.id.tv_up:
                    upOrDown = 1;
                    fly("up\n\r");
                    break;
                case R.id.tv_down:
                    upOrDown = -1;
                    fly("down\n\r");
                    break;
            }
        } else if (action == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.tv_left:
                case R.id.tv_right:
                    leftOrRight = 0;
                    fly("hStop\n\r");
                    break;
                case R.id.tv_up:
                case R.id.tv_down:
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
//        showLog(order);
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

        @Override
        public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(info, action, data);
//            showLog(info.getIp() + ":写数据成功");
        }

        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(info, action, data);
            showLog(info.getIp() + ":读数据成功 : " + new String(data.getBodyBytes()));
            parseTravelData(new String(data.getBodyBytes()));
        }
    };

    /**
     * 解析轨迹数据
     *
     * @param data
     */
    private void parseTravelData(String data) {
        if (StringUtil.isEmpty(data)) {
            return;
        }

        boolean isUpdate = false;
        if (data.contains(":update")) {
            isUpdate = true;
        }

        String[] strArr = data.split(":");
        if (strArr.length >= 3) {
            TravelBean bean = new TravelBean();
            try {
                bean.upOrDown = Integer.parseInt(strArr[0]);
                bean.leftOrRight = Integer.parseInt(strArr[1]);
                bean.paiCount = Long.parseLong(strArr[2]);
                bean.isUpdate = isUpdate;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            travelView.addTravel(bean, isUpdate);
        }
    }

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


