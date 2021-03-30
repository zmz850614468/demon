package com.lilanz.tooldemo.multiplex.download;

import android.graphics.Bitmap;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 文件下载监听
 */
public abstract class DownloadListener {

    protected void onResponse(Call call, Response response, String fileUrl, long totalLength, long alreadyDownLength) {
    }

    protected void onFailure(Call call, Exception e, String fileUrl) {
    }

    protected void onComplete(String fileUrl, String savePath) {
    }

    /**
     * 目前只用于图片下载
     *
     * @param bitmap
     */
    protected void onComplete(Bitmap bitmap) {
    }
}
