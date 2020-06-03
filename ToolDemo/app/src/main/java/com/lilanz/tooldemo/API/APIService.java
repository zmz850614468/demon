package com.lilanz.tooldemo.API;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    /**
     * 步骤一：请求开发编号列表
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("erpYpPicUpload/kfbh")
    Call<ResponseBody> getNumberList(@Body RequestBody body);

    /**
     * 图片上传接口
     * @param partLis
     * @return
     */
    @Multipart                          //这里用Multipart
    @POST("erpYpPicUpload/UploadFile")                //请求方法为POST，里面为你要上传的url
    Call<ResponseBody> uploadPic(@Part List<MultipartBody.Part> partLis);
}
