package com.demon.agv.API;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface APIService {

    /**
     * 判断是否有登录权限
     *
     * @param map Params请求方法
     * @return
     */
    @POST("cxLogin/GetAppList")
    Call<ResponseBody> hasAuthority(@QueryMap Map<String, Object> map);

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
     *
     * @param partLis
     * @return
     */
    @Multipart                          //这里用Multipart
    @POST("erpYpPicUpload/UploadFile")
    //请求方法为POST，里面为你要上传的url
    Call<ResponseBody> uploadPic(@Part List<MultipartBody.Part> partLis);

    /**
     * 获取Base64图片字符串
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("printerTemplate/setTemplateDetails")
    //请求方法为POST，里面为你要上传的url
    Call<ResponseBody> getBitmapStr(@FieldMap Map<String, String> map);
}
