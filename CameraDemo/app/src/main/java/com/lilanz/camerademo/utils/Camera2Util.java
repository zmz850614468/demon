package com.lilanz.camerademo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;

public class Camera2Util {

    public static final int REQUEST_CAMERA_CODE = 1;

    /**
     * 检查并申请相机权限
     *
     * @param activity
     * @return
     */
    public static boolean checkAndRequestPermissions(Activity activity) {
        int pe = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (pe != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
            return false;
        }

        return true;
    }

    /**
     * 默认获取后置摄像头 id
     *
     * @param manager
     * @return 返回后置摄像头id，否则为null
     */
    public static String getCameraId(CameraManager manager) {
        try {
            for (String id : manager.getCameraIdList()) {
                // 获取相机属性
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);
                // 前置或是后置摄像头
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                // 打开后置摄像头
                if (CameraCharacteristics.LENS_FACING_BACK == facing) {
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取相机的预览请求
     *
     * @param cameraDevice
     * @param surfaces
     * @return
     */
    public static CaptureRequest getPreviewRequest(CameraDevice cameraDevice, Surface[] surfaces) {
        //设置了一个具有输出Surface的CaptureRequest.Builder。
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            for (Surface surface : surfaces) {
                builder.addTarget(surface);

            }

            // 对焦模式必须设置为AUTO
//            builder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_AUTO);

            // 连续自动对焦应
            builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            // 开启相机预览并添加事件
            return builder.build();

        } catch (CameraAccessException ex) {
            ex.printStackTrace();
            Log.e("Camera2Util", "getPreviewRequest: " + ex.toString());
        }
        return null;
    }

    /**
     * 获取相机抓图请求
     *
     * @param cameraDevice
     * @param surfaces
     */
    public static CaptureRequest getCaptureRequest(CameraDevice cameraDevice, Surface[] surfaces) {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            for (Surface surface : surfaces) {
                builder.addTarget(surface);
            }
            // 使用相同的AE和AF模式作为预览。
            builder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // 对获取的图片进行旋转
            builder.set(CaptureRequest.JPEG_ORIENTATION, 90);

            return builder.build();
        } catch (CameraAccessException ex) {
            ex.printStackTrace();
            Log.e("Camera2Util", "getPreviewRequest: " + ex.toString());
        }
        return null;
    }

    /**
     * 获取相机对焦请求
     *
     * @param cameraDevice
     * @param surfaces
     * @return
     */
    public static CaptureRequest getFocusRequest(CameraDevice cameraDevice, Surface[] surfaces) {
        try {
            CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            for (Surface surface : surfaces) {
                builder.addTarget(surface);
            }
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
            builder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            return builder.build();
        } catch (CameraAccessException ex) {
            ex.printStackTrace();
            Log.e("Camera2Util", "getPreviewRequest: " + ex.toString());
        }
        return null;
    }

}
