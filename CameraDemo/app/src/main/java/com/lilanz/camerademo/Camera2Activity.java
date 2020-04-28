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
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.camerademo.utils.Camera2Util;
import com.lilanz.camerademo.utils.FileUtil;
import com.lilanz.camerademo.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera2Activity extends Activity implements View.OnClickListener {

    private TextView tvMsg;
    private Button btFocus;
    private Button btCapture;
    private Button btOpen;
    private Button btClose;
    private Button btStartVideo;
    private Button btStopVideo;
    private ImageView ivPreview;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private ImageReader imageReader;

//    private SurfaceView surfaceView2;
//    private SurfaceHolder surfaceHolder2;

    private CameraManager cameraManager;
    private CameraDevice cameraDevice;

    private CameraCaptureSession captureSession;

    private boolean hasCameraPermisson = false; // 是否有相机使用权限

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        initUI();
        hasCameraPermisson = Camera2Util.checkAndRequestPermissions(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
                openCamera2();
                break;
            case R.id.bt_close:
                closeCamera2();
                break;
            case R.id.bt_capture:
                captureRequest();
                break;
            case R.id.bt_focus:
                focusRequest();
                break;
            case R.id.bt_start_video:
                startRecordingVideo();
                break;
            case R.id.bt_stop_video:
                stopRecordingVideo();
                break;

        }
    }

    /**
     * 开始视频录制。
     */
    private void startRecordingVideo() {
        try {
            //创建录制的session会话中的请求
            CaptureRequest.Builder builder = null;
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            builder.addTarget(surfaceHolder.getSurface());
            builder.addTarget(mMediaRecorder.getSurface());
            captureSession.setRepeatingRequest(builder.build(), null, msgHandle);
            mMediaRecorder.start();
            showMsg("开始录制视频");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止视频播放
     */
    private void stopRecordingVideo() {
        try {
            captureSession.stopRepeating();
            captureSession.abortCaptures();
            captureSession.close();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        cameraDevice.close();
        showMsg("结束视频录制");
    }

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * 设置媒体录制器的配置参数
     * <p>
     * 音频，视频格式，文件路径，频率，编码格式等等
     *
     * @throws IOException
     */
    private void setUpMediaRecorder() throws IOException {
        mMediaRecorder = new MediaRecorder();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        videoPath = FileUtils.createVideoDiskFile(appContext, FileUtils.createVideoFileName()).getAbsolutePath();
        String videoPath = this.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + File.separator + StringUtil.getDataStr() + ".mp4";
        mMediaRecorder.setOutputFile(videoPath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        //每秒30帧
        mMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
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
        mMediaRecorder.prepare();
    }

    // 打开相机，并预览
    private void openCamera2() {
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);

        if (hasCameraPermisson) {
            String cameraId = Camera2Util.getCameraId(cameraManager);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                // 打开相机
                cameraManager.openCamera(cameraId, stateCallback, msgHandle);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            showToast("没有相机使用权限");
        }
    }

    // 关闭相机
    private void closeCamera2() {
        try {
            captureSession.stopRepeating();
            captureSession.close();
            cameraDevice.close();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createCameraSession() {
        if (captureSession != null) {
            captureSession.close();
        }

        try {
            setUpMediaRecorder();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CaptureSessionHelper sessionHelper = new CaptureSessionHelper(cameraDevice,
                Arrays.asList(surfaceHolder.getSurface(), imageReader.getSurface(), mMediaRecorder.getSurface()));
        sessionHelper.setCreateCaptureSesseionListener(new CaptureSessionHelper.CreateCaptureSessionListener() {
            @Override
            public void onSucceed(CameraCaptureSession cameraCaptureSession) {
                captureSession = cameraCaptureSession;
                previewRequest();
            }

            @Override
            public void onFailure(String msg) {
                showMsg(msg);
            }
        });
        sessionHelper.create();
    }

    // 发起相机预览求情
    private void previewRequest() {
        // 发送预览请求
        CaptureRequest previewRequest = Camera2Util.getPreviewRequest(cameraDevice, new Surface[]{surfaceHolder.getSurface()});
        try {
            captureSession.setRepeatingRequest(previewRequest, null, msgHandle);
            showMsg("开启相机预览并添加事件");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 发起相机对焦请求
    private void focusRequest() {
        try {
            CaptureRequest request = Camera2Util.getFocusRequest(cameraDevice, new Surface[]{surfaceHolder.getSurface()});
            captureSession.capture(request, null, msgHandle);

            // Call repeatingPreview to update mControlAFMode.
            request = Camera2Util.getPreviewRequest(cameraDevice, new Surface[]{surfaceHolder.getSurface()});
            captureSession.setRepeatingRequest(request, null, msgHandle);
            showMsg("开启相机预览并添加事件");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 摄像头连接状态回调
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            showMsg("摄像头以连接");
            cameraDevice = camera;
            createCameraSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            showMsg("摄像头断开连接");
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            showMsg("摄像头连接错误");
            camera.close();
            cameraDevice = null;
        }
    };

    Handler msgHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showMsg("handle msg");
            if (msg.obj != null) {
                showMsg(msg.obj.toString());
            }
        }
    };

    // 相机截图请求
    private void captureRequest() {
        try {
            //捕获图片
            captureSession.capture(Camera2Util.getCaptureRequest(cameraDevice, new Surface[]{imageReader.getSurface()}),
                    null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            //获取最新的一帧的Image;
            Image image = reader.acquireNextImage();

            //因为是ImageFormat.JPEG格式，所以 image.getPlanes()返回的数组只有一个，也就是第0个。
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            //ImageFormat.JPEG格式直接转化为Bitmap格式。
            final Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            showMsg("imageSize:[" + temp.getWidth() + "," + temp.getHeight() + "]");
            ivPreview.setImageBitmap(temp);

            // 保存图片
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    FileUtil.saveBitmap(Camera2Activity.this, temp);
                }
            };
            thread.start();

            // 一定需要close，否则不会收到新的Image回调。
            image.close();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Camera2Util.REQUEST_CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraPermisson = true;
                }
        }
    }

    private void initUI() {
        tvMsg = findViewById(R.id.tv_msg);
        ivPreview = findViewById(R.id.iv_preview);
        btFocus = findViewById(R.id.bt_focus);
        btCapture = findViewById(R.id.bt_capture);
        btOpen = findViewById(R.id.bt_open);
        btClose = findViewById(R.id.bt_close);
        btStartVideo = findViewById(R.id.bt_start_video);
        btStopVideo = findViewById(R.id.bt_stop_video);
        btStopVideo.setOnClickListener(this);
        btStartVideo.setOnClickListener(this);
        btFocus.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btOpen.setOnClickListener(this);
        btCapture.setOnClickListener(this);
        surfaceView = findViewById(R.id.surfaceview);

        surfaceHolder = surfaceView.getHolder();

        imageReader = ImageReader.newInstance(1600, 1200, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(onImageAvailableListener, msgHandle);

//        surfaceView2 = findViewById(R.id.surfaceview2);
//        surfaceHolder2 = surfaceView2.getHolder();
    }

    private void showMsg(String msg) {
        tvMsg.append("\n" + msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
