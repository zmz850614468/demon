package com.lilanz.doublecamera.camera2;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;

import java.util.List;

public class CaptureSessionHelper {

    private CameraDevice cameraDevice;
    private List<Surface> surfaceList;

    private CreateCaptureSessionListener listener;

    public CaptureSessionHelper(CameraDevice cameraDevice, List<Surface> surfaceList) {
        this.cameraDevice = cameraDevice;
        this.surfaceList = surfaceList;
    }

    public void create() {
        try {
            //创建一个CameraCaptureSession来进行相机预览。
            cameraDevice.createCaptureSession(surfaceList,
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (listener != null) {
                                listener.onSucceed(cameraCaptureSession);
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            if (listener != null) {
                                listener.onFailure("创建失败");
                            }
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e("CaptureSessionHelper", "create: " + e.toString());
        }
    }

    public void setCreateCaptureSesseionListener(CreateCaptureSessionListener listener) {
        this.listener = listener;
    }

    public interface CreateCaptureSessionListener {
        public void onSucceed(CameraCaptureSession cameraCaptureSession);

        public void onFailure(String msg);
    }
}
