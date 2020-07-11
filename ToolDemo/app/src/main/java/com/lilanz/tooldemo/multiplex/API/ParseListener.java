package com.lilanz.tooldemo.multiplex.API;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

// 监听器：获取解析后的数据
public abstract class ParseListener<T> {
    public void jsonParsed(@NonNull List<T> beanList) {
    }

    public void jsonParsed(T t) {
    }

    // 请求成功后的回调信息
    public void onTip(String msg) {
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    // 回调的错误信息
    public abstract void onError(int errCode, String errMsg);
}