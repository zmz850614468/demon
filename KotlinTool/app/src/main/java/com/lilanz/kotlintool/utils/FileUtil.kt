package com.lilanz.kotlintool.utils

import android.content.Context
import android.os.Environment
import java.io.File

class FileUtil {
    companion object {
        val FILE_NAME: String = "demon"

        fun saveImageByte(context: Context, imageByteArray: ByteArray, path: String) {

        }

        /**
         * 获取图片保存地址
         */
        fun getPictureFile(context: Context): File? {
            var dir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
            if (dir != null && dir.mkdirs() && dir.canWrite()) {
                return File(dir, StringUtil.getDataStr() + ".jpg")
            }
            return null
        }

        /**
         * 获取视频存放地址
         */
        fun getVideoFile(context: Context): File? {
            var dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            if (dir != null && dir.mkdirs() && dir.canWrite()) {
                return File(dir, StringUtil.getDataStr() + ".mp4")
            }
            return null
        }

        /**
         * 获取视频地址名字
         */
        fun getVidoFile(context: Context, videoName: String): File? {
            val dir =
                context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            dir!!.mkdirs()
            return if (dir.canWrite()) {
                File(dir, videoName + ".mp4")
            } else null
        }

        /**
         * 删除指定文件加下的所有数据
         */
        fun deleteFile(file: File) {
            if (!file.exists()) {
                return
            } else {
                if (file.isFile) {
                    file.delete()
                    return
                }
                if (file.isDirectory) {
                    val childFile = file.listFiles()
                    if (childFile == null || childFile.isEmpty()) {
                        file.delete()
                        return
                    }
                    for (f in childFile) {
                        deleteFile(f!!)
                    }
                    file.delete()
                }
            }
        }
    }
}