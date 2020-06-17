package com.lilanz.kotlintool.utils

import android.content.Context
import android.content.SharedPreferences

class SharePreferencesUtil {
    companion object {
        private val SHARED_NAME: String = "shared"  // shared的文件名称
        private val PARTEN_ID: String = "partenId"  // 合作者id数据

        private lateinit var instance: SharedPreferences


        // 获取：合作者id
        fun getPartenId(context: Context): Int {
            return getInstance(context).getInt(PARTEN_ID, 0)
        }

        // 保存：合作者id
        fun setPartenId(context: Context, id: Int) {
            setInt(context, PARTEN_ID, id)
        }

        //=====================         一下为固定方法         ==========================

        /**
         * 保存boolean型数据
         */
        private fun setBoolean(context: Context, key: String, value: Boolean) {
            var editor = getInstance(context).edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        /**
         * 保存int型数据
         */
        private fun setInt(context: Context, key: String, value: Int) {
            var editor = getInstance(context).edit()
            editor.putInt(key, value)
            editor.commit()
        }

        /**
         * 保存String型数据
         */
        private fun setString(context: Context, key: String, value: String) {
            val editor = getInstance(context).edit()
            editor.putString(key, value)
            editor.commit()
        }

        /**
         * 获取SharedPreferences单例对象
         */
        private fun getInstance(context: Context): SharedPreferences {
            if (instance == null) {
                instance = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
            }
            return instance
        }
    }
}