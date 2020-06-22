package com.lilanz.tooldemo.camera2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Reader;
import com.google.zxing.oned.EAN13Writer;
import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.camera2.utils.Camera2Util;

public class Camera2ExaActivity extends Activity implements View.OnClickListener {

    private SurfaceView frontSurface;
    private ImageView ivCapture;
    private Button btOpen;
    private Button btClose;
    private Button btCapture;
    private Button btStartRecord;
    private Button btStopRecord;

    private Camera2Helper camera2Helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2_exa);

        initUI();
        // 相机和语音录制权限请求
        Camera2Util.checkAndRequestPermissions(this);
        // 初始化相机帮助类
        camera2Helper = new Camera2Helper(this, frontSurface);
        // 添加截图监听
        camera2Helper.setOnCamera2CallBack(new Camera2Helper.OnCamera2CallBack() {
            @Override
            public void onCaptureCallBack(Bitmap bitmap) {
                ivCapture.setImageBitmap(bitmap);
            }
        });
        // 开启视频录制功能
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                camera2Helper.openCamera(0);
                break;
            case R.id.bt_close:
                camera2Helper.closeCamera();
                break;
            case R.id.bt_capture:
                camera2Helper.capturePicture();
                break;
            case R.id.bt_start_record:
                camera2Helper.startMediaRecord();
                break;
            case R.id.bt_stop_record:
                camera2Helper.stopMediaRecord();
                break;
        }
    }

    private void initUI() {
        frontSurface = findViewById(R.id.front_surface_view);
        ivCapture = findViewById(R.id.iv_capture);
        btOpen = findViewById(R.id.bt_open);
        btClose = findViewById(R.id.bt_close);
        btCapture = findViewById(R.id.bt_capture);
        btStartRecord = findViewById(R.id.bt_start_record);
        btStopRecord = findViewById(R.id.bt_stop_record);
        btOpen.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btCapture.setOnClickListener(this);
        btStartRecord.setOnClickListener(this);
        btStopRecord.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        camera2Helper.openCamera(CameraCharacteristics.LENS_FACING_BACK);
    }

    @Override
    protected void onPause() {
        camera2Helper.onPause();
        super.onPause();
    }
}
