package com.lilanz.kotlintool.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIManager {
    companion object {
        /**
         * 网络请求的基础地址
         */
        private val BASE_PATH = "http://192.168.35.136:14000/"

        private var retrofitInstance: Retrofit? = null;

        fun getRetrofit(): Retrofit? {
            if (retrofitInstance == null) {
                retrofitInstance = Retrofit.Builder()
                    .baseUrl(BASE_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofitInstance
        }

        fun getService(clazz: Class<*>): Any? {
            return getRetrofit()?.create(clazz)
        }
    }
}