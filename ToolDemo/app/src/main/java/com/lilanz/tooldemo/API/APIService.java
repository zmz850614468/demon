package com.lilanz.tooldemo.API;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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

}
