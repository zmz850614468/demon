package com.lilanz.tooldemo.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {

    private static final String BASE_PATH = "http://192.168.35.136:14000/";

    private static Retrofit retrofitInstance;

    public static Retrofit getRetrofit() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_PATH)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofitInstance;
    }

    // 创建类的对象，并返回
    public static <T> T getService(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

}
