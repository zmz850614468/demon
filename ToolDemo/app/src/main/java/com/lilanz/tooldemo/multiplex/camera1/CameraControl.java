package com.lilanz.tooldemo.multiplex.camera1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lilanz.tooldemo.utils.BitmapUtil;
import com.lilanz.tooldemo.utils.FileUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 摄像头控制类
 */
public class CameraControl {

    private Context context;

    private SurfaceView surfaceView;
    private Surface surface;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private int cameraId = -1;

    private MediaRecorder mediaRecorder;
    private int videoWidth = 1280;  // 视频输出的默认尺寸
    private int videoHeight = 960;

    private boolean isSurfaceCreate = false;
    private boolean isCameraOpen;   // 判断摄像头是否开启
    private boolean isRecordOpen;   // 判断摄像头是否在录像

    public CameraControl(Context context, SurfaceView surfaceView) {
        this.context = context;
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();
        this.surface = surfaceHolder.getSurface();
        initCamera();
    }

    /**
     * 初始化摄像头
     */
    private void initCamera() {
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                isSurfaceCreate = true;
                if (cameraId != -1) {
                    openCamera(cameraId);
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                closeCamera();
                isSurfaceCreate = false;
            }
        });

    }

    /**
     * 打开摄像头
     *
     * @param cameraId
     */
    public void openCamera(int cameraId) {
        this.cameraId = cameraId;

        if (isCameraOpen) {
            showToast("摄像头已经开启");
            return;
        }

        if (!isSurfaceCreate) {
            showToast("surface还未初始化");
            return;
        }

        try {
            camera = Camera.open(cameraId);
            Camera.Parameters parameters = camera.getParameters();

            // 1. 预览界面，顺时针旋转90度
            camera.setDisplayOrientation(CameraUtil.getDisplayOrientation(((Activity) context), cameraId));

            // 2.设置预览大小
            List<Camera.Size> list = CameraUtil.getPreviewSize(camera);
            showLog(new Gson().toJson(list));
            if (!list.isEmpty()) {
                Camera.Size size = CameraUtil.getBestSize(list, videoWidth, videoHeight);
                showLog("目标预览大小：" + new Gson().toJson(size));

                parameters.setPreviewSize(size.width, size.height);
            }
            parameters.setPreviewFormat(ImageFormat.NV21);  // 预览格式

            // 3.设置图片大小
            list = CameraUtil.getPictureSize(camera);
            showLog(new Gson().toJson(list));
            if (!list.isEmpty()) {
                Camera.Size size = CameraUtil.getBestSize(list, videoWidth, videoHeight);
                showLog("目标图片大小：" + new Gson().toJson(size));

                parameters.setPictureSize(size.width, size.height);
            }

            // 4.设置对焦模式(设置对焦时，会报错)
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

            camera.setParameters(parameters);
            // 打开摄像头预览
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            isCameraOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拍照
     */
    private void takePic() {
        if (camera != null) {
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                }
            }, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    camera.startPreview();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "doubleCamera";
                            String name = StringUtil.getDataStr() + ".jpg";
                            File disFile = FileUtil.createIfNotExit(path, name);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                bitmap = BitmapUtil.rotate(bitmap, 270);
                            } else {
                                bitmap = BitmapUtil.rotate(bitmap, 90);
                            }
                            BitmapUtil.saveBitmap(context, bitmap, disFile.getAbsolutePath());
                            showLog("保存图片完成!");
                        }
                    }.start();
                }
            });
        }
    }

    /**
     * 拍照
     */
    public void capturePic() {
        if (camera != null) {
            camera.takePicture(new Camera.ShutterCallback() {
                @Override
                public void onShutter() {
                }
            }, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    camera.startPreview();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (cameraListener != null) {
                        cameraListener.onPictureBack(bitmap);
                    }
                }
            });
        }
    }

    /**
     * 设置视频输出尺寸
     *
     * @param width
     * @param height
     */
    public void setVideoSize(int width, int height) {
        this.videoWidth = width;
        this.videoHeight = height;
    }

    /**
     * 开始录制视频
     */
    public void startRecord(String basePath, String fileName) {
        if (isRecordOpen) {
            showToast("已经在录像中");
            return;
        }

        mediaRecorder = new MediaRecorder();

        camera.unlock();
        mediaRecorder.setCamera(camera);
//        mediaRecorder.setOrientationHint(CameraUtil.getDisplayOrientation(this, cameraId));
        // 视频输出的旋转角度
        if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            mediaRecorder.setOrientationHint(90);
        } else {
            mediaRecorder.setOrientationHint(270);
        }

        // 音视频输出流和格式，一定要按照顺序初始化
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        // 视频保存地址和文件名
        File file = FileUtil.createIfNotExit(basePath, fileName);
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setVideoSize(videoWidth, videoHeight);
        // 设置帧数率
        mediaRecorder.setVideoFrameRate(30);
        // 设置bit率
        mediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            showLog("开始录像");
            isRecordOpen = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭摄像头
     */
    private void closeCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
            isCameraOpen = false;
        }

        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecordOpen = false;
        }
        showLog("关闭摄像头:" + cameraId);
    }


    private OnCameraListener cameraListener;

    public void setCameraListener(OnCameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    /**
     * 摄像头监听类
     */
    public interface OnCameraListener {
        void onPictureBack(Bitmap bitmap);
    }

    private void showLog(String msg) {
        Log.e("cameraControl", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(((Activity) context), msg, Toast.LENGTH_SHORT).show();
    }
}
