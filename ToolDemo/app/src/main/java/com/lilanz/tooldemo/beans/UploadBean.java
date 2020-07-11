package com.lilanz.tooldemo.beans;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

/**
 * 图片上传 返回结果
 */
public class UploadBean {
    public String typeStr;
    public String zlmxidStr;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
