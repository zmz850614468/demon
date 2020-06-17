package com.lilanz.kotlintool.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIService{
    /**
     * 步骤一：请求开发编号列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("erpYpPicUpload/kfbh")
    open fun getNumberList(@Body body: RequestBody?): Call<ResponseBody?>?

    /**
     * 图片上传接口
     * @param partLis
     * @return
     */
    @Multipart //这里用Multipart
    @POST("erpYpPicUpload/UploadFile") //请求方法为POST，里面为你要上传的url
    open fun uploadPic(@Part partLis: List<MultipartBody.Part?>?): Call<ResponseBody?>?
}