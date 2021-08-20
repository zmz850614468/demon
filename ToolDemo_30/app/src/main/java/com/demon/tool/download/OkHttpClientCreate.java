package com.demon.tool.download;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * OkHttp 客户端
 */
public class OkHttpClientCreate {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient createClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .build();
        }
        return okHttpClient;
    }
}
