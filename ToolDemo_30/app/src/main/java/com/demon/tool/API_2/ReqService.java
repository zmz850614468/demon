package com.demon.tool.API_2;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 接口管理类
 */
public interface ReqService {

    /**
     * 判断是否有登录权限
     *
     * @return
     */
    @POST("svr-qyweixin/mc/getMcCount")
//    @POST("test-gw/svr-qyweixin/im/jpushmsg")
//    Call<ResponseBody> requestTest(@Body RequestBody body);
//    Observable<ResponseBody> requestTest(@Body RequestBody body);
//    Observable<Response<RequestBean>>  requestTest(@Body RequestBody body);
    Observable<ReqResponse<Map<String, Integer>>> requestTest(@Body RequestBody body);

//    Call<ResponseBody> hasAuthority(@QueryMap Map<String, Object> map);

}
