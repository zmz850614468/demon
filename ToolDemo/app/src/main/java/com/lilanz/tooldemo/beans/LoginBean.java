package com.lilanz.tooldemo.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 登录权限请求 返回结果
 */
public class LoginBean {

    public String token;
    public String uid;
    public String apsPath;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
