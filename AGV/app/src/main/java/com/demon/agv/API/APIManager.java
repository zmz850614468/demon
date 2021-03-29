package com.demon.agv.API;

import com.demon.agv.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {

    public static final String LOGIN_PATH = "http://tm.lilanz.com/";                // 登录
    public static final String LOGIN_AUTHON_PATH = "http://webt.lilang.com:8901/cxlogingetapplistproject/";   // 是否有登录权限

    public static final String APP_BASE_PATH = "http://192.168.35.136:14000/";         // 项目相关接口
    //    private static final String BASE_PATH = "http://192.168.35.90:8900/";

    // 允许多个首地址
    private static Map<String, Retrofit> retrofitMap = new HashMap<>();

    private static Retrofit getRetrofit(String path) {
        if (StringUtil.isEmpty(path)) {
            path = APP_BASE_PATH;
        }

        if (!retrofitMap.containsKey(path)) {
            Retrofit retrofitInstance = new Retrofit.Builder()
                    .baseUrl(path)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            retrofitMap.put(path, retrofitInstance);
        }

        return retrofitMap.get(path);
    }

    /**
     * 默认首地址 创建类对象，并返回
     *
     * @param clazz 需要创建的对象
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> clazz) {
        return getRetrofit(null).create(clazz);
    }

    /**
     * 特殊地址 创建类对象，并返回
     *
     * @param path  请求首地址
     * @param clazz 需要创建的对象
     * @param <T>
     * @return
     */
    public static <T> T getService(String path, Class<T> clazz) {
        return getRetrofit(path).create(clazz);
    }

}
