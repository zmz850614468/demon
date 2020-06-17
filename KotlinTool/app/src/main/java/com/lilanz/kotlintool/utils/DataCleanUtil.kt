package com.lilanz.kotlintool.utils

import android.content.Context
import java.io.File

/**
 * 清除应用一下路径的所有数据
 * "data/data/" + context.getPackageName()
 */
class DataCleanUtil {
    companion object {

        /**
         * 删除应用下的所有数据
         */
        fun deleteAppData(context: Context) {
            deleteFile(File("data/data/" + context.packageName))
        }

        /**
         * 删除文件夹下的所有 文件夹 和 文件
         */
        private fun deleteFile(file: File) {
            if (!file.exists()) {
                return
            }

            if (file.isFile) {
                file.delete()
                return
            }
            if (file.isDirectory) {
                var childFiles = file.listFiles()
                if (childFiles == null || childFiles.isEmpty()) {
                    file.delete()
                    return
                }
                for (childFile in childFiles) {
                    deleteFile(childFile)
                }
                file.delete()
            }
        }
    }
}