package com.lilanz.tooldemo.multiplex.camera1;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.StringUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demon.controls.PermissionControl;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;

    @BindView(R.id.surface_view_o)
    SurfaceView surfaceView2;

    private CameraControl cameraControl;
    private CameraControl cameraControl2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

//        new PermissionControl(this).storagePermission();
//        new PermissionControl(this).cameraPermission();
//        new PermissionControl(this).microphonePerssion();
        new PermissionControl(this).requestPermissions(new String[]{PermissionControl.STORAGE,
                PermissionControl.CAMERA, PermissionControl.MICROPHONE});

        cameraControl = new CameraControl(this, surfaceView);
        cameraControl.openCamera(0);

//        cameraControl2 = new CameraControl(this, surfaceView2);
//        cameraControl2.openCamera(1);
    }

    @OnClick({R.id.bt_take_picture, R.id.bt_open_camera, R.id.bt_record_video})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_take_picture:
                break;
            case R.id.bt_open_camera:
//                cameraControl.openCamera(1);
                break;
            case R.id.bt_record_video:
                String basePath = Environment.getExternalStorageDirectory() + File.separator + "doubleVideo";
                String fileName = StringUtil.getDataStr() + ".mp4";
                cameraControl.startRecord(basePath, fileName);
                break;
        }
    }

    private void showLog(String msg) {
        Log.e("camera", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
