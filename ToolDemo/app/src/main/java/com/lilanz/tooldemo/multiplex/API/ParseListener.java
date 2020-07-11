package com.lilanz.tooldemo.multiplex.API;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

/**
 * 监听器：获取解析后的数据
 *
 * @param <T>
 */
public abstract class ParseListener<T> {
    /**
     * @param beanList 请求结果的List对象
     */
    public void jsonParsed(@NonNull List<T> beanList) {
    }

    /**
     * @param t 请求结果的Bean对象
     */
    public void jsonParsed(T t) {
    }

    /**
     * @param jsonStr 请求结果 json字符串
     */
    public void jsonResult(String jsonStr) {
    }

    /**
     * @param msg 请求成功后的回调信息
     */
    public void onTip(String msg) {
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     * @param errCode 错误代码
     * @param errMsg  错误信息
     */
    public abstract void onError(int errCode, String errMsg);
}