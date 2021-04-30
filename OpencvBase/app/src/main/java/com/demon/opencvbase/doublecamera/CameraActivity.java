package com.demon.opencvbase.doublecamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.opencvbase.R;
import com.demon.opencvbase.control.PermissionControl;
import com.demon.opencvbase.jni_opencv.jni.BitmapNative;
import com.demon.opencvbase.jni_opencv.util.JniBitmapUtil;
import com.demon.opencvbase.util.StringUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
    }

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;

    @BindView(R.id.surface_view_o)
    SurfaceView surfaceView2;

    @BindView(R.id.iv_pic1)
    ImageView ivPic1;
    @BindView(R.id.iv_pic2)
    ImageView ivPic2;
    @BindView(R.id.iv_pic3)
    ImageView ivPic3;
    @BindView(R.id.iv_pic4)
    ImageView ivPic4;
    @BindView(R.id.iv_pic5)
    ImageView ivPic5;
    @BindView(R.id.iv_pic6)
    ImageView ivPic6;

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
        cameraControl.setVideoSize(1280, 960);
        cameraControl.openCamera(1);
        cameraControl.setCameraListener(new CameraControl.OnCameraListener() {
            @Override
            public void onPictureBack(Bitmap bitmap) {
                ivPic1.setImageBitmap(bitmap);
                ivPic2.setImageBitmap(JniBitmapUtil.bitmapToGray(bitmap));
//                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
                Bitmap rBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                Bitmap gBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                Bitmap bBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                BitmapNative.bitmapSplit(bitmap, rBitmap, gBitmap, bBitmap);
                ivPic4.setImageBitmap(rBitmap);
                ivPic5.setImageBitmap(gBitmap);
                ivPic6.setImageBitmap(bBitmap);


            }
        });

//        cameraControl2 = new CameraControl(this, surfaceView2);
//        cameraControl2.openCamera(1);
    }


    @OnClick({R.id.bt_take_picture, R.id.bt_open_camera, R.id.bt_record_video})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_take_picture:
                cameraControl.capturePic();
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
