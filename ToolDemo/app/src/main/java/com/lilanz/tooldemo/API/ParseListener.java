package com.lilanz.tooldemo.API;

import android.support.annotation.NonNull;

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

    // 回调的错误信息
    public abstract void onError(String msg);
}