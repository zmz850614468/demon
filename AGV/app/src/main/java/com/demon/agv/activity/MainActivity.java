package com.demon.agv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.demon.agv.R;
import com.demon.agv.control.PermissionControl;
import com.demon.agv.control_data.MainDataControl;
import com.demon.agv.oksocket.ClientOkSocket;
import com.demon.agv.oksocket.OnSocketListener;
import com.demon.agv.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

    public static final String DIRECTION_UP = "up";
    public static final String DIRECTION_DOWN = "down";
    public static final String DIRECTION_LEFT = "left";
    public static final String DIRECTION_RIGHT = "right";

    public static final String IP = "192.168.1.100";
    public static final int PORT = 8899;

    public static final int BASE_SPEED = 10;    // 基础速度
    private static final int MOVE_MODE = 1;     // 运行模式

    private List<String> pressedDirection = new ArrayList<>();  // 保存所有按下的按键,目前只处理正常的操作方式

    private ClientOkSocket clientOkSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.LOCATION});

        initOkSocket();
    }

    @OnTouch({R.id.tv_up, R.id.tv_down, R.id.tv_left, R.id.tv_right})
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_UP) {
            String direction = null;
            switch (v.getId()) {
                case R.id.tv_up:
                    direction = DIRECTION_UP;
                    break;
                case R.id.tv_down:
                    direction = DIRECTION_DOWN;
                    break;
                case R.id.tv_left:
                    direction = DIRECTION_LEFT;
                    break;
                case R.id.tv_right:
                    direction = DIRECTION_RIGHT;
                    break;
            }

            if (action == MotionEvent.ACTION_DOWN) {    // 开始运动
                if (!StringUtil.isEmpty(direction) && !pressedDirection.contains(direction)) {
                    pressedDirection.add(direction);
                    changeSpeed(pressedDirection);
                }
            } else {    // 结束运动
                if (pressedDirection.contains(direction)) {
                    pressedDirection.remove(direction);
                    changeSpeed(pressedDirection);
                }
            }
        }

        return true;
    }

    /**
     * 改变速度
     *
     * @param directionList
     */
    private void changeSpeed(List<String> directionList) {
        String moveOrder = MainDataControl.getSpeedOrder(BASE_SPEED, directionList, MOVE_MODE);
        showLog(moveOrder);
        clientOkSocket.sendMsg(IP, PORT, moveOrder.getBytes());
    }

    private void initOkSocket() {
        clientOkSocket = ClientOkSocket.getInstance(this);
        clientOkSocket.addListener(new OnSocketListener() {
            @Override
            protected void onMsgCallback(String ip, String msg) {
                showLog("socket received: " + msg);
            }

            @Override
            protected void onDisconnected(String ip) {
                super.onDisconnected(ip);
                if (IP.equals(ip)) {
                    runShowToast("与服务器断开连接");
                }
            }

            @Override
            protected void onConnected(String ip) {
                super.onConnected(ip);
                if (IP.equals(ip)) {
                    runShowToast("连上服务器");
                }
            }
        });
    }

    private void showLog(String msg) {
        Log.e("main", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void runShowToast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
