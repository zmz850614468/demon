package com.lilanz.tooldemo.multiplex.camera2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.camera2.utils.Camera2Util;
import com.lilanz.tooldemo.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Camera2ExaActivity extends Activity implements View.OnClickListener {

    private SurfaceView frontSurface;
    private ImageView ivCapture;
    private Button btOpen;
    private Button btClose;
    private Button btCapture;
    private Button btStartRecord;
    private Button btStopRecord;

    @BindView(R.id.et_open_camera_id)
    protected EditText etOpenCameraId;
    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.et_id)
    protected EditText etCameraId;

    private Camera2Helper camera2Helper;

    private CameraManager cameraManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2_exa);
        ButterKnife.bind(this);

        initUI();
        // 相机和语音录制权限请求
        Camera2Util.checkAndRequestPermissions(this);
        // 初始化相机帮助类
        camera2Helper = new Camera2Helper(this, frontSurface);
        // 添加截图监听
        camera2Helper.setOnCamera2CallBack(new Camera2Helper.OnCamera2CallBack() {
            @Override
            public void onCaptureCallBack(Bitmap bitmap) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                showMsg("图片大小：" + width + ";" + height);
                ivCapture.setImageBitmap(bitmap);
            }
        });
        // 开启视频录制功能
        cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open:
//                zoom("2");
                String id = etOpenCameraId.getText().toString();
                camera2Helper.openCamera(id);
                break;
            case R.id.bt_close:
                camera2Helper.closeCamera();
                break;
            case R.id.bt_capture:
                camera2Helper.capturePicture();
                break;
            case R.id.bt_start_record:
                camera2Helper.startMediaRecord();
                break;
            case R.id.bt_stop_record:
                camera2Helper.stopMediaRecord();
                break;
        }
    }

    int zoom = 10;

    @OnClick(R.id.bt_zoom)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_zoom:
                zoom--;
                camera2Helper.zoom(zoom);
                break;
        }
    }

    /**
     * 获取指定相机的缩放
     *
     * @param id
     */
    public void zoom(String id) {
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
            float maxZoom = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) * 10;
            Rect cameraRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            float a = maxZoom;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.bt_get_camera_id)
    public void onGetCameraIdsClick(View v) {
        tvMsg.setText("所有相机id\n");
        try {
            for (String s : cameraManager.getCameraIdList()) {
                showMsg(s);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.bt_get_info)
    public void onGetInfoClick(View v) {
        String cameraId = etCameraId.getText().toString();
        if (StringUtil.isEmpty(cameraId)) {
            showToast("相机id为空");
            return;
        }
        tvMsg.setText("");
        showMsg("相机id：" + cameraId);

        try {
            // 获取相机属性
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            {
                Size[] sizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(SurfaceHolder.class);
                showMsg("\n\n支持的预览大小\n");
                for (Size size : sizes) {
                    showMsg(size.toString() + "\n");
                }
                showMsg("\n");
            }


            Range<Integer> controlAECompensationRange = characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
            if (controlAECompensationRange != null) {
                int min = controlAECompensationRange.getLower();
                int max = controlAECompensationRange.getUpper();
                showMsg("亮度：[" + min + "," + max + "]");
            }

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
            // JPEG:256; PRIVATE:34;YUV_420_888:35
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.bt_reset_light)
    public void onResetLight(View v) {
        try {
            int light = Integer.parseInt(etCameraId.getText().toString());
            camera2Helper.resetLight(light);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void initUI() {
        frontSurface = findViewById(R.id.front_surface_view);
        ivCapture = findViewById(R.id.iv_capture);
        btOpen = findViewById(R.id.bt_open);
        btClose = findViewById(R.id.bt_close);
        btCapture = findViewById(R.id.bt_capture);
        btStartRecord = findViewById(R.id.bt_start_record);
        btStopRecord = findViewById(R.id.bt_stop_record);
        btOpen.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btCapture.setOnClickListener(this);
        btStartRecord.setOnClickListener(this);
        btStopRecord.setOnClickListener(this);
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

    @Override
    protected void onRestart() {
        super.onRestart();
//        camera2Helper.openCamera(CameraCharacteristics.LENS_FACING_BACK);
    }

    @Override
    protected void onPause() {
        camera2Helper.onPause();
        super.onPause();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
        showLog(msg);
    }

    private void showLog(String msg) {
        Log.e("Camera2ExaActivity", msg);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
