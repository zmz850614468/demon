package com.lilanz.camerademo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.lilanz.camerademo.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private TextView tvMsg;                 // 提示信息
    private Button btRecordVideo;           // 录制视频按钮
    private Button btCapture;               // 截取图片按钮
    private ImageView ivPreview;            // 预览截图

    private SurfaceView surfaceView;        // 摄像头预览
    private SurfaceHolder surfaceHolder;    //
    private Camera camera;                  // 摄像头

    private MediaRecorder mediaRecorder;    // 录制视频的类
    private boolean isRecord = false; // 是否正在录制true录制中 false未录制

    private boolean canCapturePic = false;    // 是否要截图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // 选择支持半透明模式,在有surfaceview的activity中使用。
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        initUI();
        openCamera();
    }

    int index = 0;

    private void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.CAMERA}, 1);
            }
        }

        int count = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Size maxSize = null;
        // 获取最大像素的，相机 index
        Camera tempCamere = null;
        for (int i = 0; i < count; i++) {
            Camera.getCameraInfo(i, cameraInfo);

            try {
                camera = Camera.open(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
//            if (i == 0) {
//                tempCamere = camera;
//                maxSize = StringUtil.getMaxSize(camera.getParameters().getSupportedPreviewSizes());
//            } else {
//                Size tempSize = StringUtil.getMaxSize(camera.getParameters().getSupportedPreviewSizes());
//                if (maxSize.width < tempSize.width && maxSize.height < tempSize.height) {
//                    maxSize = tempSize;
//                    index = i;
//                    tempCamere.release();
//                    tempCamere = camera;
//                } else {
//                    camera.release();
//                }
//            }
        }
//        camera = tempCamere;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        Camera.getCameraInfo(0, cameraInfo);
//        camera = Camera.open(0);
//        deal(camera);
    }

    private Size disSize;

    public Camera deal(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        disSize = StringUtil.getMaxSize(parameters.getSupportedPreviewSizes());

//        disSize.width = 2048;
//        disSize.height = 1536;

        // 640:480 ; 1600:1200 ； 2048:1536 ; 3840*2160
        // 不用的摄像头支持不同的分辨率
        parameters.setPreviewSize(disSize.width, disSize.height);
//        parameters.setPreviewSize(1600, 1200);

        camera.setParameters(parameters);

        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                // 截图某一帧图片信息
                if (canCapturePic) {
                    Camera.Size previewSize = camera.getParameters().getPreviewSize();
                    YuvImage image = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, stream);
                    final Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                    ivPreview.setImageBitmap(bmp);
                    canCapturePic = false;

                    // 保存图片到本地内存，可以放到线程中处理
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            String path = StringUtil.getPictureFile(".png").getAbsolutePath();
                            StringUtil.saveBitmap(bmp, path);
                        }
                    };
                    thread.start();
                }
            }
        });

        return camera;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_capture:
                canCapturePic = true;
                break;
            case R.id.bt_record_video:
                recordVideo();
                break;
        }
    }

    private void initUI() {
        tvMsg = findViewById(R.id.tv_msg);
        btRecordVideo = findViewById(R.id.bt_record_video);
        surfaceView = findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        btRecordVideo.setOnClickListener(this);

        btCapture = findViewById(R.id.bt_capture);
        ivPreview = findViewById(R.id.iv_preview);
        btCapture.setOnClickListener(this);

    }

    public void recordVideo() {
        // 开始录制/停止录制
        if (isRecord) {
            showMsg("结束视频录制");
            isRecord = false;
            if (mediaRecorder != null) {
                // 停止录制
                mediaRecorder.stop();
                // 释放资源
                mediaRecorder.release();
                mediaRecorder = null;
            }
//            if (camera != null) {
//                camera.release();
//            }
//            camera = Camera.open(index);
//            deal(camera);
//            camera.setDisplayOrientation(270);
//            try {
//                // 设置camera预览的角度，因为默认图片是倾斜90度的
//                camera.setPreviewDisplay(surfaceHolder);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            camera.startPreview();
        } else {
            showMsg("录制视频中...");
            isRecord = true;
            mediaRecorder = new MediaRecorder();

            camera.unlock();
            mediaRecorder.setCamera(camera);

            // 设置录制视频源为Camera(相机)
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 设置录制完成后视频的封装格式  THREE_GPP为".3gp"  MPEG_4为".mp4"
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置录制的视频编码h263 h264
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
            // 640:480; 1280:960; 1920:1440
            mediaRecorder.setVideoSize(1280, 960);
//            mediaRecorder.setVideoSize(176, 144);
            //设置最大录像时间 单位：毫秒
            mediaRecorder.setMaxDuration(60 * 60 * 1000);
            //设置最大录制的大小 单位，字节
            mediaRecorder.setMaxFileSize(1024 * 1024);
            //音频一秒钟包含多少数据位
//            mediaRecorder.setAudioEncodingBitRate(128);

            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
//            mediaRecorder.setVideoFrameRate(20);

            // 设置视频文件输出的路径
            String path = StringUtil.getPictureFile(".mp4").getAbsolutePath();
            showMsg("path=" + path);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            try {
                // 准备录制
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
        // 设置camera预览的角度，因为默认图片是倾斜90度的
//        camera.setDisplayOrientation(270);
        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        camera.startPreview();

        Size size = camera.getParameters().getPreviewSize();
        showMsg("摄像头预览分辨率：[" + size.width + "," + size.height + "]");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.release();
        }
        surfaceView = null;
        surfaceHolder = null;
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        super.onDestroy();
    }

    public void showMsg(String msg) {
        tvMsg.append("\n" + msg);
    }
}
