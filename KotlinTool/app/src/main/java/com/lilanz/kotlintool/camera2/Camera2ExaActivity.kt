package com.lilanz.kotlintool.camera2

import android.app.Activity
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.lilanz.kotlintool.R
import com.lilanz.kotlintool.camera2.utils.Camera2Util

class Camera2ExaActivity : Activity(), View.OnClickListener {

    private var frontSurface: SurfaceView? = null
    private var ivCapture: ImageView? = null
    private var btOpen: Button? = null
    private var btClose: Button? = null
    private var btCapture: Button? = null
    private var btStartRecord: Button? = null
    private var btStopRecord: Button? = null

    private var camera2Helper: Camera2Helper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2_exa)
        initUI()
        // 相机和语音录制权限请求
        Camera2Util.checkAndRequestPermissions(this)
        // 初始化相机帮助类
        camera2Helper = Camera2Helper(this, frontSurface)
        // 添加截图监听
        camera2Helper!!.setOnCamera2CallBack(object : Camera2Helper.OnCamera2CallBack {
            override fun onCaptureCallBack(bitmap: Bitmap?) {
                ivCapture!!.setImageBitmap(bitmap)
            }
        })
        // 开启视频录制功能
        camera2Helper!!.setCanRecord(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_open -> camera2Helper!!.openCamera(0)
            R.id.bt_close -> camera2Helper!!.closeCamera()
            R.id.bt_capture -> camera2Helper!!.capturePicture()
            R.id.bt_start_record -> camera2Helper!!.startMediaRecord()
            R.id.bt_stop_record -> camera2Helper!!.stopMediaRecord()
        }
    }

    private fun initUI() {
        frontSurface = findViewById(R.id.front_surface_view)
        ivCapture = findViewById(R.id.iv_capture)
        btOpen = findViewById(R.id.bt_open)
        btClose = findViewById(R.id.bt_close)
        btCapture = findViewById(R.id.bt_capture)
        btStartRecord = findViewById(R.id.bt_start_record)
        btStopRecord = findViewById(R.id.bt_stop_record)
        btOpen?.setOnClickListener(this)
        btClose?.setOnClickListener(this)
        btCapture?.setOnClickListener(this)
        btStartRecord?.setOnClickListener(this)
        btStopRecord?.setOnClickListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        camera2Helper!!.openCamera(CameraCharacteristics.LENS_FACING_BACK)
    }

    override fun onPause() {
        camera2Helper!!.onPause()
        super.onPause()
    }
}