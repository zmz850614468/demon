package com.lilanz.tooldemo.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 请求开发编号 返回结果
 */
public class NumberBean {
    public String dm;
    public String mc;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
