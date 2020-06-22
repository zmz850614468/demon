package com.lilanz.kotlintool.camera2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.drawable.ColorDrawable
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.media.MediaRecorder
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Surface
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import com.lilanz.kotlintool.R
import com.lilanz.kotlintool.camera2.utils.Camera2Util
import com.lilanz.kotlintool.utils.FileUtil
import com.lilanz.kotlintool.utils.StringUtil
import java.io.File
import java.io.IOException
import java.util.*

class Camera2Helper {
    companion object {
        private const val TAG = "Camera2Helper"
        // 截图的分辨率
        private const val CAPTURE_WIDTH = 1280
        private const val CAPTURE_HEIGHT = 960
        // 视频录制的分辨率
        private const val VIDEO_WIDTH = 640
        private const val VIDEO_HEIGHT = 480
        /**
         * 相机的状态
         * 0：处于未使用状态
         * 1：处于开启摄像头状态
         * 2：开启摄像头成功，处于预览状态
         * 3：处于视频录制状态
         */
        const val CAMERA_STATUS_INIT = 0
        const val CAMERA_STATUS_OPENING = 1
        const val CAMERA_STATUS_OPENED = 2
        const val CAMERA_STATUS_RECORDING = 3
    }


    // 摄像头
    private lateinit var cameraSurface: SurfaceView
    private lateinit var cameraManager: CameraManager
    private var cameraDevie: CameraDevice? = null
    private var cameraSession: CameraCaptureSession? = null
    private var cameraImageReader: ImageReader? = null

    private var cameraStatus = CAMERA_STATUS_INIT // 用于记录当前相机的状态


    private var mediaRecorder // 相机的录制功能
            : MediaRecorder? = null
    private var canRecord = false // 是否可以录制视频,默认不开启视频录制功能

    private var videoPath // 视频保存路径
            : String? = null
    private val videoName // 视频文件名，可有可无
            : String? = null

    private var activity: Activity? = null
    private var onCamera2CallBack // 用于相机的回调监听
            : OnCamera2CallBack? = null

    constructor(activity: Activity, surfaceView: SurfaceView?) {
        cameraSurface = surfaceView!!
        this.activity = activity
        cameraManager =
            activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    // 打开摄像头
    fun openCamera(id: Int) {
        val cameraId: String? =
            cameraManager?.let { Camera2Util.getCameraId(it, id.toString() + "") }
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        try {
            if (cameraId != null && cameraStatus == CAMERA_STATUS_INIT) {
                cameraStatus = CAMERA_STATUS_OPENING
                cameraSurface.visibility = View.VISIBLE
                cameraManager.openCamera(cameraId, cameraStateCallBack, null)
            } else if (cameraId == null) {
                showToast("摄像头不存在")
            } else {
                showToast("摄像头已经开启")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        cameraSurface.background = null
    }

    // 开启摄像头的回调监听
    private val cameraStateCallBack: CameraDevice.StateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraStatus = CAMERA_STATUS_OPENED
                cameraDevie = camera
                createPreviewSession()
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraStatus = CAMERA_STATUS_INIT
                camera.close()
                Log.d(TAG, "摄像头断开连接 ")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                cameraStatus = CAMERA_STATUS_INIT
                camera.close()
                Log.d(TAG, "打开摄像头错误 ")
            }
        }

    // 开启摄像头会话
    private fun createPreviewSession() { // 添加摄像头录制功能
        if (canRecord) {
            try {
                initMediaRecorder()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // 添加截图功能
        cameraImageReader = ImageReader.newInstance(
            CAPTURE_WIDTH,
            CAPTURE_HEIGHT,
            ImageFormat.JPEG,
            5
        )
        cameraImageReader?.setOnImageAvailableListener(onImageAvailableListener, null)
        val surfaceList: MutableList<Surface> =
            ArrayList()
        surfaceList.add(cameraSurface.holder.surface)
        cameraImageReader?.surface?.let { surfaceList.add(it) }
        if (canRecord) {
            surfaceList.add(mediaRecorder!!.surface)
        }
        val sessionHelper = CaptureSessionHelper(cameraDevie, surfaceList)
        sessionHelper.setCreateCaptureSesseionListener(object :
            CaptureSessionHelper.CreateCaptureSessionListener {
            override fun onSucceed(cameraCaptureSession: CameraCaptureSession?) {
                cameraSession = cameraCaptureSession
                // 开启摄像头预览
                val previewRequest: CaptureRequest? = cameraDevie?.let {
                    Camera2Util.getPreviewRequest(
                        it,
                        arrayOf(cameraSurface.holder.surface)
                    )
                }
                try {
                    cameraSession!!.setRepeatingRequest(previewRequest, null, null)
                    Log.d(TAG, "开启摄像头预览")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(msg: String?) {
                Log.d(TAG, "onFailure: $msg")
            }
        })
        sessionHelper.create()
    }

    // 图片截取监听
    private val onImageAvailableListener =
        OnImageAvailableListener { reader ->
            // 保存图片
            val thread: Thread = object : Thread() {
                override fun run() {
                    super.run()
                    //获取最新的一帧的Image;
                    val image = reader.acquireNextImage()
                    if (onCamera2CallBack != null) { //因为是ImageFormat.JPEG格式，所以 image.getPlanes()返回的数组只有一个，也就是第0个。
                        val byteBuffer =
                            image.planes[0].buffer
                        val ImageBytes =
                            ByteArray(byteBuffer.remaining())
                        byteBuffer[ImageBytes]
                        val bitmap =
                            BitmapFactory.decodeByteArray(ImageBytes, 0, ImageBytes.size)
                        activity!!.runOnUiThread {
                            // TODO 显示截图结果
                            onCamera2CallBack!!.onCaptureCallBack(bitmap)
                        }
                        // 一定需要close，否则不会收到新的Image回调。
                    }
                    image.close()
                }
            }
            thread.start()
        }

    //
    fun capturePicture() {
        if ((cameraStatus == CAMERA_STATUS_OPENED || cameraStatus == CAMERA_STATUS_RECORDING)
            && cameraSession != null
        ) {
            try { //捕获图片
                showToast("开始截图")
                cameraDevie?.let {
                    cameraSession!!.capture(
                        Camera2Util.getCaptureRequest(
                            it,
                            arrayOf(cameraImageReader?.surface)
                        ),
                        null, null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置媒体录制器的配置参数
     *
     *
     * 音频，视频格式，文件路径，频率，编码格式等等
     *
     * @throws IOException
     */
    @Throws(Exception::class)
    private fun initMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        if (videoPath == null) { // 用户没有设置地址，则使用默认地址
            videoPath = activity?.let { FileUtil.getVideoFile(it)?.absolutePath }
        }
        mediaRecorder!!.setOutputFile(videoPath)
        mediaRecorder!!.setVideoEncodingBitRate(10000000)
        //每秒16帧
        mediaRecorder!!.setVideoFrameRate(16)
        //        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mediaRecorder!!.setVideoSize(VIDEO_WIDTH, VIDEO_HEIGHT)
        mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
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
        mediaRecorder!!.prepare()
    }

    /**
     * 开启后置摄像头录制
     */
    fun startMediaRecord() {
        if (!canRecord) {
            showToast("视频录制功能没有开启")
            return
        }
        if (cameraStatus == CAMERA_STATUS_OPENED && cameraSession != null) {
            cameraStatus = CAMERA_STATUS_RECORDING
            try {
                val builder =
                    cameraDevie?.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                builder?.addTarget(cameraSurface.holder.surface)
                builder?.addTarget(mediaRecorder!!.surface)
                cameraSession!!.setRepeatingRequest(builder?.build(), null, null)
                mediaRecorder!!.start()
                //                isRecording = true;
                showToast("开始录制视频")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (cameraStatus == CAMERA_STATUS_INIT) {
            showToast("摄像头未打开")
        } else if (cameraStatus == CAMERA_STATUS_RECORDING) {
            showToast("摄像头正在录制中...")
        } else {
            showToast("录制视频失败")
        }
    }


    /**
     * 停止视频播放
     */
    fun stopMediaRecord() {
        if (cameraStatus == CAMERA_STATUS_RECORDING) {
            videoPath = null // 正常保存视频后，置空视频路径
            closeCamera()
            showToast("结束视频录制")
        }
    }

    /**
     * 关闭摄像头相关资源
     */
    fun closeCamera() {
        if (cameraStatus != CAMERA_STATUS_INIT) {
            if (cameraImageReader != null) {
                cameraImageReader?.close()
                cameraImageReader = null
            }
            if (cameraSession != null) {
                try {
                    cameraSession?.stopRepeating()
                    cameraSession?.abortCaptures()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                cameraSession?.close()
                cameraSession = null
            }
            if (mediaRecorder != null && cameraStatus == CAMERA_STATUS_RECORDING) {
                mediaRecorder!!.stop()
                mediaRecorder!!.reset()
                mediaRecorder = null
            }
            if (cameraDevie != null) {
                cameraDevie?.close()
                cameraDevie = null
            }
            cameraStatus = CAMERA_STATUS_INIT
            cameraSurface.background =
                ColorDrawable(activity!!.resources.getColor(R.color.colorBlack))
            deleteVideoFile(videoPath)
            videoPath = null
        }
    }

    /**
     * 删除存在的视频文件(不正常关闭视频的文件全部删除)
     *
     * @param videoPath
     */
    private fun deleteVideoFile(videoPath: String?) {
        if (StringUtil.isEmpty(videoPath)) {
            return
        }
        val file = File(videoPath)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * 界面不可用的时候一定要关闭摄像头资源
     */
    fun onPause() {
        closeCamera()
        deleteVideoFile(videoPath)
    }

    /**
     * 设置视频文件名，返回整个视频路径
     *
     * @param videoName
     * @return
     */
    fun setVideoName(videoName: String?): String? {
        videoPath = activity?.let { FileUtil.getVideoFile(it, videoName)?.getAbsolutePath() }
        return videoPath
    }

    fun setOnCamera2CallBack(callBack: OnCamera2CallBack?) {
        onCamera2CallBack = callBack
    }

    interface OnCamera2CallBack {
        fun onCaptureCallBack(bitmap: Bitmap?)
    }

    fun setCanRecord(canRecord: Boolean) {
        this.canRecord = canRecord
    }


    private fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

}