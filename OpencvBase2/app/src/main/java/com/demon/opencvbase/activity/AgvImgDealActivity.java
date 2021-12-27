package com.demon.opencvbase.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demon.opencvbase.R;
import com.demon.opencvbase.control.PermissionControl;
import com.demon.opencvbase.control_ui.AgvTestUi;
import com.demon.opencvbase.doublecamera.CameraActivity;
import com.demon.opencvbase.doublecamera.CameraControl;
import com.demon.opencvbase.jni_opencv.jni.AgvDealNative;
import com.demon.opencvbase.jni_opencv.jni.BitmapNative;
import com.demon.opencvbase.util.BitmapUtil;
import com.demon.opencvbase.util.RootUtil;
import com.demon.opencvbase.util.SerialPortUtil;
import com.demon.opencvbase.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android_serialport_api.SerialPortUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AgvImgDealActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    // 串口 和波特率
    public static final String PORT_PATH_485_1 = "/dev/ttyS1";
    public static final int PORT_BAUDRATE_485_1 = 115200;

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    @BindView(R.id.iv_pic1)
    ImageView ivPic1;
    @BindView(R.id.iv_pic2)
    ImageView ivPic2;
    @BindView(R.id.iv_pic3)
    ImageView ivPic3;

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    private CameraControl cameraControl;

    private SerialPortUtils serialPort;   // 串口

    private AgvTestUi agvTestUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agv_img_deal);
        ButterKnife.bind(this);

        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.CAMERA, PermissionControl.MICROPHONE});

        initCamera();
        initUI();

        if (RootUtil.isRoot(this)) {
            initSerialPort();
        }

//        initTest();

//        tvMsg.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                takePic(true);
//            }
//        }, 500);
    }

    @OnClick({R.id.bt_take_picture, R.id.bt_start, R.id.bt_stop})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_take_picture:
                cameraControl.capturePic();
                break;
            case R.id.bt_start:
                takePic(true);
                break;
            case R.id.bt_stop:
                takePic(false);
                break;
        }
    }

    private Timer takePicTimer;
    private boolean isTakingPic;

    /**
     * 控制拍照
     *
     * @param isStart
     */
    private void takePic(boolean isStart) {
        if (isStart) {
            if (takePicTimer == null) {
                takePicTimer = new Timer();
                takePicTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (cameraControl.isCameraOpen() && !isTakingPic) {
                            isTakingPic = true;
                            cameraControl.capturePic();
                        }
                    }
                }, 100, 1000);
            }
        } else {
            if (takePicTimer != null) {
                takePicTimer.cancel();
                takePicTimer = null;
            }
        }
    }

    int index = 0;

    private void agvDeal(Bitmap src) {
        ivPic1.setImageBitmap(src);
//        Bitmap des = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Bitmap des = src.copy(src.getConfig(), src.isMutable());
        String result = AgvDealNative.bitmapDeal(des);
        ivPic2.setImageBitmap(des);

        index++;
        if (!StringUtil.isEmpty(result) && result.contains("&")) {
            String str[] = result.split("&");
            sendMsg(Double.parseDouble(str[0]), Double.parseDouble(str[1]));
            saveBitmap(src);
        } else {
            showMsg("没有找到垂点:" + result + " ; " + index);
//            saveBitmap(src);
        }

        showLog("center: " + result);
    }

    private void initCamera() {
        cameraControl = new CameraControl(this, surfaceView);
        cameraControl.setVideoSize(320, 240);
        cameraControl.openCamera(0);
        cameraControl.setCameraListener(bitmap -> {
            agvDeal(bitmap);
            isTakingPic = false;
        });
    }

    private void initUI() {
//        originalBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.type_1);
//        ivPic1.setImageBitmap(originalBitmap);

    }

    /**
     * 测试用
     */
    private void initTest() {
//        agvTestUi = new AgvTestUi(this);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gray_1);
//        agvTestUi.testBitmap(bitmap);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_104);
        agvDeal(bitmap.copy(bitmap.getConfig(), bitmap.isMutable()));

//        Bitmap des = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
//        BitmapNative.bitmap2gray2(bitmap, des);
//        saveBitmap(des);

    }

    private void sendMsg(double x, double y) {
        double rr = x * x + y * y;
        int r = (int) (rr / (Math.sqrt(rr) - x));
        StringBuffer buffer = new StringBuffer();
        buffer.append("AA 21");
        buffer.append(String.format(" %08x", ((int) (x * 100))));
        buffer.append(String.format(" %08x", ((int) (y * 100))));
        if (y == 0) {
            buffer.append(" ffffffff");
        } else {
            buffer.append(String.format(" %08x", r));
        }
        buffer.append(" 55 2F");

        if (serialPort != null) {
            serialPort.sendSerialPort(SerialPortUtil.hexStr2Byte(buffer.toString()));
        }
//        showMsg("serial : " + buffer.toString());

        showMsg("(" + x + "," + y + ")  -- " + r + "   ; " + index);
    }

    private void initSerialPort() {
        serialPort = new SerialPortUtils();
        serialPort.openSerialPort(this, PORT_PATH_485_1, PORT_BAUDRATE_485_1);
    }

    private void saveBitmap(Bitmap bitmap) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        path = path + File.separator + StringUtil.getDataStr() + ".jpg";
        BitmapUtil.saveBitmap(this, bitmap, path);
    }


    private void showLog(String msg) {
        Log.e("AgvImgDealActivity", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showMsg(String msg) {
        if (tvMsg.getText().length() > 400) {
            tvMsg.setText(msg + "\n");
            return;
        }

        tvMsg.append(msg + "\n");
    }
}
