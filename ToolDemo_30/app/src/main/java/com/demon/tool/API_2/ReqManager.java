package com.demon.tool.API_2;


import com.demon.tool.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 的管理类
 */
public class ReqManager {

    public static final String APP_BASE_PATH = "http://webt.lilang.com:8901/";         // 项目相关接口
//    public static final String APP_BASE_PATH = "http://webt.lilang.com/";         // 项目相关接口

    // 允许多个首地址
    private static Map<String, Retrofit> retrofitMap = new HashMap<>();

    private static Retrofit getRetrofit(String path) {
        if (StringUtil.isEmpty(path)) {
            path = APP_BASE_PATH;
        }

        if (!retrofitMap.containsKey(path)) {
            // 初始化okhttp
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Retrofit retrofitInstance = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(path)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

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
     * @param rootPath  请求首地址
     * @param clazz 需要创建的对象
     * @param <T>
     * @return
     */
    public static <T> T getService(String rootPath, Class<T> clazz) {
        return getRetrofit(rootPath).create(clazz);
    }

}
