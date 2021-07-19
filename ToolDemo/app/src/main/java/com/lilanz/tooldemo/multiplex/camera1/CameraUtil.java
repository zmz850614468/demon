package com.lilanz.tooldemo.multiplex.camera1;

import android.app.Activity;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import java.util.List;

/**
 * 摄像头工具类
 */
public class CameraUtil {


    /**
     * 获取摄像头总数
     *
     * @return
     */
    public static int getCameraCount() {
        // 旧的API返回的摄像头个数最多两个
        return Camera.getNumberOfCameras();
    }

    /**
     * 获取支持的预览大小
     *
     * @param camera
     * @return
     */
    public static List<Camera.Size> getPreviewSize(@NonNull Camera camera) {
        List<Camera.Size> list = camera.getParameters().getSupportedPreviewSizes();
        return list;
    }

    /**
     * 获取支持的图片大小
     *
     * @param camera
     * @return
     */
    public static List<Camera.Size> getPictureSize(@NonNull Camera camera) {
        List<Camera.Size> list = camera.getParameters().getSupportedPictureSizes();
        return list;
    }

    /**
     * 获取与目标大小最匹配的预览尺寸
     *
     * @param sizeList
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Camera.Size getBestSize(List<Camera.Size> sizeList, int targetWidth, int targetHeight) {
        Camera.Size bestSize = null;

        float targetRatio = (targetWidth * 1.0f) / targetHeight;    // 目标大小的宽高比
        float minDiff = targetRatio;

        for (Camera.Size size : sizeList) {
            // 1.如果找到一致的大小，则返回对应数据
            if (size.width == targetWidth && size.height == targetHeight) {
                bestSize = size;
                break;
            }

            // 2.获取宽高比例，误差最小的预览大小
            float supportedRatio = (size.width * 1.0f) / size.height;
            if (Math.abs(supportedRatio - targetRatio) < minDiff) {
                minDiff = Math.abs(supportedRatio - targetRatio);
                bestSize = size;
            }
        }

        return bestSize;
    }

    /**
     * 获取摄像头的选择角度
     *
     * @param activity
     * @param cameraId
     * @return
     */
    public static int getDisplayOrientation(Activity activity, int cameraId) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();   // 屏幕的旋转角度

        int screenDegree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                screenDegree = 0;
                break;
            case Surface.ROTATION_90:
                screenDegree = 90;
                break;
            case Surface.ROTATION_180:
                screenDegree = 180;
                break;
            case Surface.ROTATION_270:
                screenDegree = 270;
                break;
        }

        int displayOrientation = 0;
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            displayOrientation = (cameraInfo.orientation + screenDegree) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - screenDegree + 360) % 360;
        }
        showLog("摄像头的原始角度：" + cameraInfo.orientation);
        showLog("屏幕的旋转角度 : " + screenDegree);
        showLog("需要旋转的角度：" + displayOrientation);

        return displayOrientation;
    }

    private static void showLog(String msg) {
        Log.e("cameraUtil", msg);
    }
}
