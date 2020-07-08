package com.lilanz.tooldemo.multiplex.camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.camera2.utils.Camera2Util;
import com.lilanz.tooldemo.utils.FileUtil;
import com.lilanz.tooldemo.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Camera2Helper {

    private static final String TAG = "Camera2Helper";
    // 截图的分辨率
    private static final int CAPTURE_WIDTH = 1280;
    private static final int CAPTURE_HEIGHT = 960;
    // 视频录制的分辨率
    private static final int VIDEO_WIDTH = 640;
    private static final int VIDEO_HEIGHT = 480;


    // 摄像头
    private SurfaceView cameraSurface;
    private CameraManager cameraManager;
    private CameraDevice cameraDevie;
    private CameraCaptureSession cameraSession;
    private ImageReader cameraImageReader;

    /**
     * 相机的状态
     * 0：处于未使用状态
     * 1：处于开启摄像头状态
     * 2：开启摄像头成功，处于预览状态
     * 3：处于视频录制状态
     */
    public static final int CAMERA_STATUS_INIT = 0;
    public static final int CAMERA_STATUS_OPENING = 1;
    public static final int CAMERA_STATUS_OPENED = 2;
    public static final int CAMERA_STATUS_RECORDING = 3;
    private int cameraStatus = CAMERA_STATUS_INIT;  // 用于记录当前相机的状态

    private MediaRecorder mediaRecorder;        // 相机的录制功能
    private boolean canRecord = false;          // 是否可以录制视频,默认不开启视频录制功能
    private String videoPath;                   // 视频保存路径
    private String videoName;                   // 视频文件名，可有可无

    private Activity activity;
    private OnCamera2CallBack onCamera2CallBack;    // 用于相机的回调监听

    public Camera2Helper(Activity activity, SurfaceView surfaceView) {
        this.cameraSurface = surfaceView;
        this.activity = activity;
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
    }

    // 打开摄像头
    public void openCamera(int id) {
        String cameraId = Camera2Util.getCameraId(cameraManager, id + "");
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            if (cameraId != null && cameraStatus == CAMERA_STATUS_INIT) {
                cameraStatus = CAMERA_STATUS_OPENING;
                cameraSurface.setVisibility(View.VISIBLE);
                cameraManager.openCamera(cameraId, cameraStateCallBack, null);
            } else if (cameraId == null) {
                showToast("摄像头不存在");
            } else {
                showToast("摄像头已经开启");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraSurface.setBackground(null);
    }

    // 开启摄像头的回调监听
    private CameraDevice.StateCallback cameraStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraStatus = CAMERA_STATUS_OPENED;
            cameraDevie = camera;
            createPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraStatus = CAMERA_STATUS_INIT;
            camera.close();
            Log.d(TAG, "摄像头断开连接 ");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraStatus = CAMERA_STATUS_INIT;
            camera.close();
            Log.d(TAG, "打开摄像头错误 ");
        }
    };

    // 开启摄像头会话
    private void createPreviewSession() {
        // 添加摄像头录制功能
        if (canRecord) {
            try {
                initMediaRecorder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 添加截图功能
        cameraImageReader = ImageReader.newInstance(CAPTURE_WIDTH, CAPTURE_HEIGHT, ImageFormat.JPEG, 5);
        cameraImageReader.setOnImageAvailableListener(onImageAvailableListener, null);

        List<Surface> surfaceList = new ArrayList<>();
        surfaceList.add(cameraSurface.getHolder().getSurface());
        surfaceList.add(cameraImageReader.getSurface());
        if (canRecord) {
            surfaceList.add(mediaRecorder.getSurface());
        }

        CaptureSessionHelper sessionHelper = new CaptureSessionHelper(cameraDevie, surfaceList);
        sessionHelper.setCreateCaptureSesseionListener(new CaptureSessionHelper.CreateCaptureSessionListener() {
            @Override
            public void onSucceed(CameraCaptureSession cameraCaptureSession) {
                cameraSession = cameraCaptureSession;
                // 开启摄像头预览
                CaptureRequest previewRequest = Camera2Util.getPreviewRequest(cameraDevie, new Surface[]{cameraSurface.getHolder().getSurface()});
                try {
                    cameraSession.setRepeatingRequest(previewRequest, null, null);
                    Log.d(TAG, "开启摄像头预览");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.d(TAG, "onFailure: " + msg);
            }
        });
        sessionHelper.create();
    }

    // 图片截取监听
    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(final ImageReader reader) {
            // 保存图片
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    //获取最新的一帧的Image;

                    Image image = reader.acquireNextImage();
                    if (onCamera2CallBack != null) {
                        //因为是ImageFormat.JPEG格式，所以 image.getPlanes()返回的数组只有一个，也就是第0个。
                        ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
                        byte[] ImageBytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(ImageBytes);
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.length);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO 显示截图结果
                                onCamera2CallBack.onCaptureCallBack(bitmap);
                            }
                        });
                        // 一定需要close，否则不会收到新的Image回调。
                    }
                    image.close();
                }
            };
            thread.start();
        }
    };

    //
    public void capturePicture() {
        if ((cameraStatus == CAMERA_STATUS_OPENED || cameraStatus == CAMERA_STATUS_RECORDING)
                && cameraSession != null) {
            try {
                //捕获图片
                showToast("开始截图");
                cameraSession.capture(Camera2Util.getCaptureRequest(cameraDevie, new Surface[]{cameraImageReader.getSurface()}),
                        null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置媒体录制器的配置参数
     * <p>
     * 音频，视频格式，文件路径，频率，编码格式等等
     *
     * @throws IOException
     */
    private void initMediaRecorder() throws Exception {
        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        if (videoPath == null) {    // 用户没有设置地址，则使用默认地址
            videoPath = FileUtil.getVideoFile(activity).getAbsolutePath();
        }
        mediaRecorder.setOutputFile(videoPath);
        mediaRecorder.setVideoEncodingBitRate(10000000);
        //每秒16帧
        mediaRecorder.setVideoFrameRate(16);
//        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mediaRecorder.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//        switch (mSensorOrientation) {
//            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
//                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
//                break;
//            case SENSOR_ORIENTATION_INVERSE_DEGREES:
//                mMediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation));
//                break;
//            default:
//                break;
//        }
        mediaRecorder.prepare();
    }

    /**
     * 开启后置摄像头录制
     */
    public void startMediaRecord() {
        if (!canRecord) {
            showToast("视频录制功能没有开启");
            return;
        }
        if (cameraStatus == CAMERA_STATUS_OPENED && cameraSession != null) {
            cameraStatus = CAMERA_STATUS_RECORDING;
            try {
                CaptureRequest.Builder builder = cameraDevie.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.addTarget(cameraSurface.getHolder().getSurface());
                builder.addTarget(mediaRecorder.getSurface());
                cameraSession.setRepeatingRequest(builder.build(), null, null);
                mediaRecorder.start();
//                isRecording = true;
                showToast("开始录制视频");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cameraStatus == CAMERA_STATUS_INIT) {
            showToast("摄像头未打开");
        } else if (cameraStatus == CAMERA_STATUS_RECORDING) {
            showToast("摄像头正在录制中...");
        } else {
            showToast("录制视频失败");
        }
    }


    /**
     * 停止视频播放
     */
    public void stopMediaRecord() {
        if (cameraStatus == CAMERA_STATUS_RECORDING) {
            videoPath = null;   // 正常保存视频后，置空视频路径
            closeCamera();
            showToast("结束视频录制");
        }
    }

    /**
     * 关闭摄像头相关资源
     */
    public void closeCamera() {
        if (cameraStatus != CAMERA_STATUS_INIT) {
            if (cameraImageReader != null) {
                cameraImageReader.close();
                cameraImageReader = null;
            }
            if (cameraSession != null) {
                try {
                    cameraSession.stopRepeating();
                    cameraSession.abortCaptures();
                } catch (Exception e){
                    e.printStackTrace();
                }
                cameraSession.close();
                cameraSession = null;
            }
            if (mediaRecorder != null && cameraStatus == CAMERA_STATUS_RECORDING) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder = null;
            }
            if (cameraDevie != null) {
                cameraDevie.close();
                cameraDevie = null;
            }
            cameraStatus = CAMERA_STATUS_INIT;
            cameraSurface.setBackground(new ColorDrawable(activity.getResources().getColor(R.color.colorBlack)));
            deleteVideoFile(videoPath);
            videoPath = null;
        }
    }

    /**
     * 删除存在的视频文件(不正常关闭视频的文件全部删除)
     *
     * @param videoPath
     */
    private void deleteVideoFile(String videoPath) {
        if (StringUtil.isEmpty(videoPath)) {
            return;
        }
        File file = new File(videoPath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 界面不可用的时候一定要关闭摄像头资源
     */
    public void onPause() {
        closeCamera();
        deleteVideoFile(videoPath);
    }

    /**
     * 设置视频文件名，返回整个视频路径
     *
     * @param videoName
     * @return
     */
    public String setVideoName(String videoName) {
        videoPath = FileUtil.getVideoFile(activity, videoName).getAbsolutePath();
        return videoPath;
    }

    public void setOnCamera2CallBack(OnCamera2CallBack callBack) {
        this.onCamera2CallBack = callBack;
    }

    public interface OnCamera2CallBack {
        public void onCaptureCallBack(Bitmap bitmap);
    }

    public void setCanRecord(boolean canRecord) {
        this.canRecord = canRecord;
    }


    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}
