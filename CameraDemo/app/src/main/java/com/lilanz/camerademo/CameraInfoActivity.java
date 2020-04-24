package com.lilanz.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.widget.TextView;

public class CameraInfoActivity extends Activity {

    private TextView tvMsg;

    private CameraManager cameraManager;
    private CameraDevice cameraDevice;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_info);

        initUI();
        showCameraInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCameraInfo() {
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                showMsg("相机id:" + id);
                // 获取相机属性
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                // 前置或是后置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                showMsg("摄像头方向(1:后置;0:前置)：" + facing);
                // 是否支持闪光灯
                boolean canUseFlash = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                showMsg("是否支持闪光灯：" + canUseFlash);
                // 最大调焦值
                float maxZoom = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                showMsg("最大调焦值:" + maxZoom);
                // 最小调焦值
                float minZoom = characteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
                showMsg("最小调焦值:" + minZoom);

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                // 输入模式
                int[] inputFormatArr = map.getInputFormats();
                for (int i : inputFormatArr) {
                    Size[] size = map.getInputSizes(i);
                    showMsg("inputFormat:" + i);
                    for (Size size1 : size) {
                        showMsg("[" + size1.getWidth() + "," + size1.getHeight() + "]");
                    }
                }
                // 输出模式
                StringBuffer buffer = new StringBuffer();
                int[] outFormatArr = map.getOutputFormats();
                for (int i : outFormatArr) {
                    Size[] sizes = map.getOutputSizes(i);
                    showMsg("outputFormat:" + i);
                    for (Size size : sizes) {
                        buffer = new StringBuffer();
                        buffer.append("size=[" + size.getWidth() + "," + size.getHeight() + "]");
                        long minFrame = map.getOutputMinFrameDuration(i, size);
                        long stall = map.getOutputStallDuration(i, size);
                        buffer.append("  MinFrameDuration=" + minFrame);
                        buffer.append("  StallDuration=" + stall);
                        showMsg(buffer.toString());
                    }
                }
                // 高速视频 speedVideo
                Range<Integer>[] fpsArr = map.getHighSpeedVideoFpsRanges();
                for (Range<Integer> range : fpsArr) {
                    buffer = new StringBuffer();
                    buffer.append("speedVideo: range=[" + range.getLower() + "," + range.getUpper() + "]");
                    Size[] sizes = map.getHighSpeedVideoSizesFor(range);
                    for (Size size : sizes) {
                        buffer.append(" [" + size.getWidth() + "," + size.getHeight() + "]");
                    }
                    showMsg(buffer.toString());
                }
                // 硬件支持等级： LEVEL_3 > FULL > LIMIT > LEGACY
                // 当支持到 FULL 等级的相机设备，将拥有比旧 API 强大的新特性，
                // 如 30fps 全高清连拍，帧之间的手动设置，RAW 格式的图片拍摄，
                // 快门零延迟以及视频速拍等，否则和旧 API 功能差别不大。
//                int hardwareLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                showMsg("硬件支持等级：" + isHardwareSupported(characteristics));


            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "CameraInfoActivity";
    private int isHardwareSupported(CameraCharacteristics characteristics) {
        Integer deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == null) {
            Log.e(TAG, "can not get INFO_SUPPORTED_HARDWARE_LEVEL");
            return -1;
        }
        switch (deviceLevel) {
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL:
                Log.w(TAG, "hardware supported level:LEVEL_FULL");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY:
                Log.w(TAG, "hardware supported level:LEVEL_LEGACY");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3:
                Log.w(TAG, "hardware supported level:LEVEL_3");
                break;
            case CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED:
                Log.w(TAG, "hardware supported level:LEVEL_LIMITED");
                break;
        }
        return deviceLevel;
    }

    private void initUI() {
        tvMsg = findViewById(R.id.tv_msg);
    }

    private void showMsg(String msg) {
        tvMsg.append("\n" + msg);
    }
}
