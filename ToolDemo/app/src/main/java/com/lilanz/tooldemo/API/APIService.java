package com.lilanz.tooldemo.API;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    /**
     * 视频数据上传成功接口
     *
     * @param body
     * @return
     */
    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("svr-ecommodity/eVideoStatus/setVideoStatus")
    Call<ResponseBody> uploadVideoSucceed(@Body RequestBody body);
}
