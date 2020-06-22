package com.lilanz.kotlintool.camera2

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.util.Log
import android.view.Surface

class CaptureSessionHelper {

    private var cameraDevice: CameraDevice? = null
    private var surfaceList: List<Surface>? = null

    private var listener: CreateCaptureSessionListener? = null

    constructor(cameraDevice: CameraDevice?, surfaceList: List<Surface>?) {
        this.cameraDevice = cameraDevice
        this.surfaceList = surfaceList
    }

    fun create() {
        try { //创建一个CameraCaptureSession来进行相机预览。
            cameraDevice!!.createCaptureSession(
                surfaceList,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        if (listener != null) {
                            listener!!.onSucceed(cameraCaptureSession)
                        }
                    }

                    override fun onConfigureFailed(
                        cameraCaptureSession: CameraCaptureSession
                    ) {
                        if (listener != null) {
                            listener!!.onFailure("创建失败")
                        }
                    }
                }, null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("CaptureSessionHelper", "create: $e")
        }
    }

    fun setCreateCaptureSesseionListener(listener: CreateCaptureSessionListener) {
        this.listener = listener
    }

    interface CreateCaptureSessionListener {
        fun onSucceed(cameraCaptureSession: CameraCaptureSession?)
        fun onFailure(msg: String?)
    }
}