package com.lilanz.kotlintool.api

import com.lilanz.kotlintool.utils.StringUtil
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIManager {
    companion object {
        /**
         * 网络请求的基础地址
         */
        const val BASE_PATH: String = "http://192.168.35.136:14000/"          // 样衣拍照
        const val LOGIN_BASE_PATH: String = "http://webt.lilang.com:8901/";   // 是否有登录权限

        private var retrofitMap: HashMap<String?, Retrofit> = HashMap<String?, Retrofit>();
//        private var retrofitInstance: Retrofit? = null;

        fun getRetrofit(path: String?): Retrofit? {
            var tempPath = path;
            if (StringUtil.isEmpty(tempPath)) {
                tempPath = BASE_PATH
            }

            if (!retrofitMap.containsKey(tempPath)) {
                var instance = Retrofit.Builder()
                    .baseUrl(tempPath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitMap.put(tempPath, instance);
            }

            return retrofitMap.get(tempPath)
        }

        fun getService(clazz: Class<*>): Any? {
            return getRetrofit("")?.create(clazz)
        }

        fun getService(path: String?, clazz: Class<*>): Any? {
            return getRetrofit(path)?.create(clazz)
        }
    }
}