package com.lilanz.kotlintool.camera2.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.Surface

class Camera2Util {
    companion object{
        const val REQUEST_CAMERA_CODE = 1

        /**
         * 检查并申请相机权限
         *
         * @param activity
         * @return
         */
        fun checkAndRequestPermissions(activity: Activity?): Boolean {
            val ca =
                ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
            val au = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.RECORD_AUDIO
            )
            if (ca != PackageManager.PERMISSION_GRANTED || au != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.CAMERA
                        , Manifest.permission.RECORD_AUDIO
                    ), REQUEST_CAMERA_CODE
                )
                return false
            }
            return true
        }

        /**
         * 默认获取后置摄像头 id
         *
         * @param manager
         * @return 返回后置摄像头id，否则为null
         */
        fun getCameraId(manager: CameraManager): String? {
            try {
                for (id in manager.cameraIdList) { // 获取相机属性
                    val characteristics =
                        manager.getCameraCharacteristics(id)
                    // 前置或是后置摄像头
                    val facing = characteristics.get(
                        CameraCharacteristics.LENS_FACING
                    )
                    val map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                    )
                    // 打开后置摄像头 TODO
                    if (CameraCharacteristics.LENS_FACING_BACK == facing) {
                        return id
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取指定cameraId，
         *
         * @param manager
         * @return 存在返回cameraId，否则返回null
         */
        fun getCameraId(
            manager: CameraManager,
            cameraId: String
        ): String? {
            try {
                for (id in manager.cameraIdList) {
                    if (id == cameraId) {
                        return id
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 打开指定位置的摄像头：前置或是后置摄像头
         *
         * @param manager
         * @param cameraFacing
         * @return
         */
        fun getCameraId(
            manager: CameraManager,
            cameraFacing: Int
        ): String? {
            try {
                for (id in manager.cameraIdList) {
                    val characteristics =
                        manager.getCameraCharacteristics(id)
                    val facing = characteristics.get(
                        CameraCharacteristics.LENS_FACING
                    )
                    if (facing == cameraFacing) {
                        return id
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        /**
         * 获取相机的预览请求
         *
         * @param cameraDevice
         * @param surfaces
         * @return
         */
        fun getPreviewRequest(
            cameraDevice: CameraDevice,
            surfaces: Array<Surface?>
        ): CaptureRequest? { //设置了一个具有输出Surface的CaptureRequest.Builder。
            try {
                val builder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                )
                for (surface in surfaces) {
                    builder.addTarget(surface)
                }
                // 对焦模式必须设置为AUTO
//            builder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_AUTO);
// 连续自动对焦应
                builder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                // 开启相机预览并添加事件
                return builder.build()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("Camera2Util", "getPreviewRequest: $ex")
            }
            return null
        }

        /**
         * 获取相机抓图请求
         *
         * @param cameraDevice
         * @param surfaces
         */
        fun getCaptureRequest(
            cameraDevice: CameraDevice,
            surfaces: Array<Surface?>
        ): CaptureRequest? {
            try {
                val builder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE
                )
                for (surface in surfaces) {
                    builder.addTarget(surface)
                }
                // 使用相同的AE和AF模式作为预览。
                builder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                )
                // 对获取的图片进行旋转
//            builder.set(CaptureRequest.JPEG_ORIENTATION, 90);
                return builder.build()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("Camera2Util", "getPreviewRequest: $ex")
            }
            return null
        }

        /**
         * 获取相机对焦请求
         *
         * @param cameraDevice
         * @param surfaces
         * @return
         */
        fun getFocusRequest(
            cameraDevice: CameraDevice,
            surfaces: Array<Surface?>
        ): CaptureRequest? {
            try {
                val builder = cameraDevice.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
                )
                for (surface in surfaces) {
                    builder.addTarget(surface)
                }
                builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
                builder.set(
                    CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_AUTO
                )
                builder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
                )
                return builder.build()
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("Camera2Util", "getPreviewRequest: $ex")
            }
            return null
        }

    }
}