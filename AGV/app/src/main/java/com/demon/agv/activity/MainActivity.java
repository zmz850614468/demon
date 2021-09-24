package com.demon.agv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.agv.R;
import com.demon.agv.bean.StepBean;
import com.demon.agv.control.PermissionControl;
import com.demon.agv.control_data.MainDataControl;
import com.demon.agv.oksocket.ClientOkSocket;
import com.demon.agv.oksocket.OnSocketListener;
import com.demon.agv.util.SharePreferencesUtil;
import com.demon.agv.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class MainActivity extends AppCompatActivity {

    public static final String DIRECTION_UP = "front";
    public static final String DIRECTION_DOWN = "back";
    public static final String DIRECTION_LEFT = "left";
    public static final String DIRECTION_RIGHT = "right";

    @BindView(R.id.et_ip)
    EditText etIp;

    @BindView(R.id.tv_zq)
    TextView tvZQ;
    @BindView(R.id.tv_z1)
    TextView tvZ1;
    @BindView(R.id.tv_z2)
    TextView tvZ2;

    //    public static final String IP = "192.168.1.142";
    public static String IP = "192.168.43.119";
    public static final int PORT = 81;

    public static final int BASE_SPEED = 10;    // 基础速度
    private static final int MOVE_MODE = 1;     // 运行模式

    private List<String> pressedDirection = new ArrayList<>();  // 保存所有按下的按键,目前只处理正常的操作方式

    private ClientOkSocket clientOkSocket;

//    String DIALOG_USB_PERMISSION = "com.example.usbtest.GRANT_USB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.LOCATION});

        initUI();
//        initOkSocket();
//        initTest();

//        //弹出权限申请窗口
//        PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(DIALOG_USB_PERMISSION), 0);
//        usbManager.requestPermission(usbDevice, usbPermissionIntent);

//        int a = -8 % 5;
//        int b = 8 % 5;
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
        } else if (action == MotionEvent.ACTION_MOVE) {
            changeSpeed(pressedDirection);
        }

        return true;
    }

    /**
     * 改变速度
     *
     * @param directionList
     */
    private void changeSpeed(List<String> directionList) {
//        String moveOrder = MainDataControl.getSpeedOrder(BASE_SPEED, directionList, MOVE_MODE);
//        showLog(moveOrder);
//        clientOkSocket.sendMsg(IP, PORT, moveOrder.getBytes());

        String direction = MainDataControl.getDirection(directionList);
        clientOkSocket.sendMsg(IP, PORT, direction.getBytes());
        showLog("direction=" + direction);
    }

    private float dis1 = -1;
    private float dis2 = -1;
    private float dis3 = -1;

    private void initOkSocket() {
        clientOkSocket = ClientOkSocket.getInstance(this);
        clientOkSocket.addListener(new OnSocketListener() {
            @Override
            protected void onMsgCallback(String ip, String msg) {
//                showLog("socket received: " + msg);
                if (msg != null && msg.contains("dis1:")) {
                    String dis = msg.substring(5, msg.length());
                    try {
                        dis1 = Float.parseFloat(dis);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else if (msg != null && msg.contains("dis2:")) {
                    String dis = msg.substring(5, msg.length());
                    try {
                        dis2 = Float.parseFloat(dis);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

//                    showLog("dis1=" + dis1 + " ; dis2=" + dis2);
                } else if (msg != null && msg.contains("dis3:")) {
                    String dis = msg.substring(5, msg.length());
                    try {
                        dis3 = Float.parseFloat(dis);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvZQ.setText("左前：" + dis3);
                            tvZ1.setText("左1：" + dis1);
                            tvZ2.setText("左2：" + dis2);
                        }
                    });
//                    showLog("dis3=" + dis3);
                    if (isContinue) {
                        showLog(stepCount + ": isContinue dis1=" + dis1 + " ; dis2=" + dis2);
                        stepCount++;
                        changeDir();
//                        isContinue = false;
                    }

                }

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

        clientOkSocket.sendMsg(IP, PORT, "".getBytes());
    }

    private void initUI() {
        String ip = SharePreferencesUtil.getServiceIp(this);
        if (!StringUtil.isEmpty(ip)) {
            IP = ip;
        }
        etIp.setText(IP);
    }

    // TODO 处理超声波数据

    private int minDis = 10;
    private int maxDis = 25;

    private boolean isLeftTurning = false;   // 是否需要左拐弯
    private boolean isRightTuring = false;   // 是否需要右拐弯

    /**
     * 1.直行
     * 2.左前方
     * 3.右前方
     * 4.原地左转
     * 5.原地右转
     * 6.停止
     */
    private void changeDir() {
        if (dis1 == -1 || dis2 == -1 || dis3 == -1) {
            showLog("dis : 超声波测距出问题( " + dis1 + " : " + dis2 + ")");
            dis1 = -1;
            dis2 = -1;
            dis3 = -1;
            return;
        }


//        { // 判断是否需要右转弯

        if (isRightTuring && dis3 >= maxDis) {
            isRightTuring = false;
        }

        if (isRightTuring || dis3 < minDis) {
            isRightTuring = true;
            isLeftTurning = false;
            clientOkSocket.sendMsg(IP, PORT, "originRight".getBytes());
            return;
        }

//        }

//        { // 判断是否需要左转弯
        if (!isLeftTurning && (dis1 - dis2) > 20) {
            isLeftTurning = true;
        }

        if (isLeftTurning && dis1 < maxDis) {
            isLeftTurning = false;
        }

        if (isLeftTurning) {
            clientOkSocket.sendMsg(IP, PORT, "left".getBytes());
            return;
        }
//        }

        // 1.差值大于固定值，需要转弯
        if (dis1 - dis2 >= 0) { // 车尾更靠墙
            if (dis1 < minDis) {    // 距离小于最小值：直线直走
                clientOkSocket.sendMsg(IP, PORT, "front".getBytes());
            } else if (dis1 < maxDis) { // 距离在控制范围内：需要调整方向
                if (dis1 - dis2 < 5) {  // 直走
                    clientOkSocket.sendMsg(IP, PORT, "front".getBytes());
                } else {                 // 左前方运动
                    clientOkSocket.sendMsg(IP, PORT, "left".getBytes());
                }
            } else {                    // 距离超出大于最大值：原地左转弯
                clientOkSocket.sendMsg(IP, PORT, "originLeft".getBytes());
            }
        } else if (dis1 - dis2 < 0) {   // 车头更靠墙
            if (dis1 < minDis) {        // 距离小于最小值：原地右转弯
                clientOkSocket.sendMsg(IP, PORT, "originRight".getBytes());
            } else if (dis1 < maxDis) { // 距离在控制范围内：需要调整方向
                if (dis1 - dis2 > -5) { // 直走
                    clientOkSocket.sendMsg(IP, PORT, "front".getBytes());
                } else {                // 右前方运动
                    clientOkSocket.sendMsg(IP, PORT, "right".getBytes());
                }
            } else {                    // 距离超出大于最大值：直走
                clientOkSocket.sendMsg(IP, PORT, "front".getBytes());
            }
        }

//        if (dis1 > minDis && dis1 < maxDis && dis2 > minDis && dis2 < maxDis) {
//            if (Math.abs(dis1 - dis2) <= 1) {
//                ;
//            }
//        }

        dis1 = -1;
        dis2 = -1;
        dis3 = -1;
    }

    @Override
    protected void onDestroy() {
        String ip = etIp.getText().toString();
        SharePreferencesUtil.saveServiceIp(this, ip);
        super.onDestroy();
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

    // ===============================    测试用      =============================
    @BindView(R.id.et_time)
    EditText etTime;

    private boolean isContinue = false;
    private int stepCount = 0;

    private List<StepBean> stepList = new ArrayList<>();

    private void initTest() {
        sendMsgThread.start();
    }

    List<String> directionList = new ArrayList<>();

    @OnClick({R.id.bt_step_front, R.id.bt_start_or_stop, R.id.bt_scan})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_step_front:
                try {
                    int time = Integer.parseInt(etTime.getText().toString());
                    directionList.clear();
                    directionList.add(DIRECTION_UP);
                    String direction = MainDataControl.getDirection(directionList);
                    StepBean bean = new StepBean();
                    bean.msg = direction;
                    bean.during = time;
                    addBean(bean);
                    addStop();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_start_or_stop:
                isContinue = !isContinue;
                break;
            case R.id.bt_scan:
                Intent intent = new Intent(this, ScanAcrtivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addStop() {
        directionList.clear();
        String direction = MainDataControl.getDirection(directionList);
        StepBean bean = new StepBean();
        bean.msg = direction;
        bean.during = 100;
        addBean(bean);
    }

    private synchronized void addBean(StepBean bean) {
        stepList.add(bean);
    }

    private synchronized StepBean getBean() {
        if (stepList.isEmpty()) {
            return null;
        }
        return stepList.remove(0);
    }

    private Thread sendMsgThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (true) {
                StepBean bean = getBean();
                if (bean != null) {
                    showLog("step : " + bean.msg + " : " + bean.during);
                    clientOkSocket.sendMsg(IP, PORT, bean.msg.getBytes());
                    try {
                        Thread.sleep(bean.during);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };


}
