package com.lilanz.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.camerademo.utils.Camera2Util;
import com.lilanz.camerademo.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DoubleCameraActivity extends Activity implements View.OnClickListener {

    private SurfaceView frontSurface;
    private ImageView ivFront;
    private Button btOpen;
    private Button btClose;
    private Button btCapture;

    private SurfaceView backSurface;
    private ImageView ivBack;
    private Button btOpenBack;
    private Button btCloseBack;
    private Button btCaptureBack;
    private Button btStartRecordBack;
    private Button btStopRecordBack;

    private TextView tvMsg;


    private CameraManager cameraManager;
    private CameraDevice frontCamera;
    private CameraCaptureSession frontSession;
    private ImageReader frontImageReader;
    private String frontCamearId = null;     // 打开的前置摄像头id


    private CameraDevice backCamera;
    private CameraCaptureSession backSession;
    private ImageReader backImageReader;
    private String backCamearId = null;      // 打开的后置摄像头id
    private boolean isBackRecording = false; // 后置摄像头是否在录制视频
    private String backVideoPath = null;     // 后置摄像头的视频保存路径
    /**
     * MediaRecorder
     */
    private MediaRecorder backMediaRecorder;   // 后置摄像头添加录像功能

    // 0:打开前置摄像头； 1:打开后置摄像头
    private int openCameraFacing;
    // 0:前置摄像头截图； 1:后置摄像头截图
    private int captureFacing;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_camera);

        initUI();
        Camera2Util.checkAndRequestPermissions(this);   // 相机权限请求
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
    }

    private Handler msgHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                openFrontCamera();
                break;
            case R.id.bt_close:
                closeFrontCamera();
                break;
            case R.id.bt_capture:
                captureFacing = 0;
                frontCaptureRequest();
                break;


            case R.id.bt_open_back:
                openBackCamera();
                break;
            case R.id.bt_close_back:
                closeBackCamera();
                break;
            case R.id.bt_capture_back:
                captureFacing = 1;
                backCaptureRequest();
                break;
            case R.id.bt_start_video_back:
                startBackMediaRecord();
                break;
            case R.id.bt_stop_video_back:
                stopBackMediaRecord();
                break;
        }
    }

    // 打开前置摄像头
    private void openFrontCamera() {
        String cameraId = Camera2Util.getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_FRONT, backCamearId);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            if (cameraId != null) {
                openCameraFacing = 0;
                cameraManager.openCamera(cameraId, cameraStateCallBack, msgHandle);
            } else {
                showToast("前置摄像头不存在");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback cameraStateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            if (openCameraFacing == 0) {
                frontCamearId = camera.getId();
                frontCamera = camera;
                createFrontPreviewSession();
            } else if (openCameraFacing == 1) {
                backCamearId = camera.getId();
                backCamera = camera;
                createBackPreviewSession();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            showMsg("摄像头断开连接");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            showMsg("打开摄像头错误");
            deleteInvalidFile();
        }
    };

    // 开启前置摄像头会话
    private void createFrontPreviewSession() {
        frontImageReader = ImageReader.newInstance(1280, 960, ImageFormat.JPEG, 2);
        frontImageReader.setOnImageAvailableListener(onImageAvailableListener, null);

        CaptureSessionHelper sessionHelper = new CaptureSessionHelper(frontCamera,
                Arrays.asList(frontSurface.getHolder().getSurface(), frontImageReader.getSurface()));
        sessionHelper.setCreateCaptureSesseionListener(new CaptureSessionHelper.CreateCaptureSessionListener() {
            @Override
            public void onSucceed(CameraCaptureSession cameraCaptureSession) {
                frontSession = cameraCaptureSession;
                // 开启前置摄像头预览
                CaptureRequest previewRequest = Camera2Util.getPreviewRequest(frontCamera, new Surface[]{frontSurface.getHolder().getSurface()});
                try {
                    frontSession.setRepeatingRequest(previewRequest, null, msgHandle);
                    showMsg("开启前置摄像头预览");
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                showMsg(msg);
            }
        });
        sessionHelper.create();
    }

    // 关闭前置摄像头
    private void closeFrontCamera() {
        frontCamearId = null;
        if (frontImageReader != null) {
            frontImageReader.close();
            frontImageReader = null;
        }
        if (frontSession != null) {
            try {
                frontSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            frontSession.close();
            frontSession = null;
        }
        if (frontCamera != null) {
            frontCamera.close();
            frontCamera = null;
            showMsg("关闭前置摄像头");
        }
        if (frontImageReader != null) {
            frontImageReader.close();
            frontImageReader = null;
        }
    }

    // 前置摄像头截图请求
    private void frontCaptureRequest() {
        showMsg("前摄像头截图");
        try {
            //捕获图片
            frontSession.capture(Camera2Util.getCaptureRequest(frontCamera, new Surface[]{frontImageReader.getSurface()}),
                    null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 打开后置摄像头
    private void openBackCamera() {

        String cameraId = Camera2Util.getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_BACK, frontCamearId);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            if (cameraId != null) {
                openCameraFacing = 1;
                cameraManager.openCamera(cameraId, cameraStateCallBack, msgHandle);
            } else {
                showToast("后置摄像头不存在");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 开启后置摄像头会话
    private void createBackPreviewSession() {
        backImageReader = ImageReader.newInstance(1280, 960, ImageFormat.JPEG, 2);
        backImageReader.setOnImageAvailableListener(onImageAvailableListener, null);
        try {
            initBackMediaRecorder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        CaptureSessionHelper sessionHelper = new CaptureSessionHelper(backCamera,
                Arrays.asList(backSurface.getHolder().getSurface(), backImageReader.getSurface(), backMediaRecorder.getSurface()));
        sessionHelper.setCreateCaptureSesseionListener(new CaptureSessionHelper.CreateCaptureSessionListener() {
            @Override
            public void onSucceed(CameraCaptureSession cameraCaptureSession) {
                backSession = cameraCaptureSession;
                // 开启后置摄像头预览
                CaptureRequest previewRequest = Camera2Util.getPreviewRequest(backCamera, new Surface[]{backSurface.getHolder().getSurface()});
                try {
                    backSession.setRepeatingRequest(previewRequest, null, msgHandle);
                    showMsg("开启后置摄像头预览");
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                showMsg(msg);
            }
        });
        sessionHelper.create();
    }

    // 关闭后置摄像头
    private void closeBackCamera() {
        backCamearId = null;
        if (backImageReader != null) {
            backImageReader.close();
            backImageReader = null;
        }
        if (backSession != null) {
            try {
                backSession.stopRepeating();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            backSession.close();
            backSession = null;
        }
        if (backCamera != null) {
            backCamera.close();
            backCamera = null;
            showMsg("关闭后置摄像头");
        }
        if (backImageReader != null) {
            backImageReader.close();
            backImageReader = null;
        }
    }

    // 前置摄像头截图请求
    private void backCaptureRequest() {
        showMsg("后摄像头截图");
        try {
            //捕获图片
            backSession.capture(Camera2Util.getCaptureRequest(backCamera, new Surface[]{backImageReader.getSurface()}),
                    null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置媒体录制器的配置参数
     * <p>
     * 音频，视频格式，文件路径，频率，编码格式等等
     *
     * @throws IOException
     */
    private void initBackMediaRecorder() throws IOException {
        backMediaRecorder = new MediaRecorder();

        backMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        backMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        backMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        backVideoPath = FileUtil.getVideoFile(this).getAbsolutePath();
        backMediaRecorder.setOutputFile(backVideoPath);
        backMediaRecorder.setVideoEncodingBitRate(10000000);
        //每秒30帧
        backMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        backMediaRecorder.setVideoSize(640, 480);
        backMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        backMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
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
        backMediaRecorder.prepare();
    }

    /**
     * 开启后置摄像头录制
     */
    private void startBackMediaRecord() {
        if (backSession != null) {
            try {
                isBackRecording = true;
                CaptureRequest.Builder builder = backCamera.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
                builder.addTarget(backSurface.getHolder().getSurface());
                builder.addTarget(backMediaRecorder.getSurface());
                backSession.setRepeatingRequest(builder.build(), null, msgHandle);
                backMediaRecorder.start();
                showMsg("开始后置摄像头录制视频");
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止视频播放
     */
    private void stopBackMediaRecord() {
        if (backSession != null) {
            try {
                backSession.stopRepeating();
                backSession.abortCaptures();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            backSession.close();
            backSession = null;
        }
        if (backMediaRecorder != null) {
            backMediaRecorder.stop();
            backMediaRecorder.reset();
            backMediaRecorder = null;
        }
        if (backCamera != null) {
            backCamera.close();
            backCamera = null;
        }
        isBackRecording = false;
        backVideoPath = null;
        showMsg("结束后置摄像头视频录制");
    }


    private void initUI() {
        tvMsg = findViewById(R.id.tv_msg);
        // 前置摄像头相关
        frontSurface = findViewById(R.id.front_surcace);
        ivFront = findViewById(R.id.iv_front);
        btOpen = findViewById(R.id.bt_open);
        btClose = findViewById(R.id.bt_close);
        btCapture = findViewById(R.id.bt_capture);
        btCapture.setOnClickListener(this);
        btOpen.setOnClickListener(this);
        btClose.setOnClickListener(this);

        // 后置摄像头相关
        backSurface = findViewById(R.id.back_surcace);
        ivBack = findViewById(R.id.iv_back);
        btOpenBack = findViewById(R.id.bt_open_back);
        btCloseBack = findViewById(R.id.bt_close_back);
        btCaptureBack = findViewById(R.id.bt_capture_back);
        btStartRecordBack = findViewById(R.id.bt_start_video_back);
        btStopRecordBack = findViewById(R.id.bt_stop_video_back);
        btStartRecordBack.setOnClickListener(this);
        btStopRecordBack.setOnClickListener(this);
        btCaptureBack.setOnClickListener(this);
        btOpenBack.setOnClickListener(this);
        btCloseBack.setOnClickListener(this);


    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //获取最新的一帧的Image;
            Image image = reader.acquireLatestImage();

            //因为是ImageFormat.JPEG格式，所以 image.getPlanes()返回的数组只有一个，也就是第0个。
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            //ImageFormat.JPEG格式直接转化为Bitmap格式。
            final Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            showMsg("imageSize:[" + temp.getWidth() + "," + temp.getHeight() + "]");

            // TODO 存在现实错误的情况
            if (captureFacing == 0) {
                ivFront.setImageBitmap(temp);
            } else if (captureFacing == 1) {
                ivBack.setImageBitmap(temp);
            }

//            // 保存图片
//            Thread thread = new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    FileUtil.saveBitmap(Camera2Activity.this, temp);
//                }
//            };
//            thread.start();

            // 一定需要close，否则不会收到新的Image回调。
            image.close();
        }
    };

    @Override
    protected void onDestroy() {
        if (frontImageReader != null) {
            frontImageReader.close();
            frontImageReader = null;
        }
        if (frontSession != null) {
            frontSession.close();
            frontSession = null;
        }
        if (frontCamera != null) {
            frontCamera.close();
            frontCamera = null;
        }

        if (backImageReader != null) {
            backImageReader.close();
            backImageReader = null;
        }
        if (backSession != null) {
            backSession.close();
            backSession = null;
        }
        if (backCamera != null) {
            backCamera.close();
            backCamera = null;
        }

        deleteInvalidFile();

        super.onDestroy();
    }

    // 删除无用的视频文件
    private void deleteInvalidFile() {
        // 删除没有使用的视频文件或是没有正常关闭的视频文件
        if (backVideoPath != null) {
            File file = new File(backVideoPath);
            if (!isBackRecording && file.exists()) {
                file.delete();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 点击后退键时，优先关闭预览的视频
        if (frontSession != null) {
            try {
                frontSession.stopRepeating();
                frontSession.abortCaptures();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        if (backSession != null) {
            try {
                backSession.stopRepeating();
                backSession.abortCaptures();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        if (backMediaRecorder != null && isBackRecording) {
            backMediaRecorder.stop();
            backMediaRecorder.reset();
        }
        super.onBackPressed();
    }

    private void showMsg(String msg) {
        tvMsg.append("\n" + msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
