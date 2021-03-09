package com.lilanz.tooldemo.multiplex.download;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 文件下载监听
 */
public interface DownloadListener {

    void onResponse(Call call, Response response, String fileUrl, long totalLength, long alreadyDownLength);

    void onFailure(Call call, Exception e, String fileUrl);

    void onComplete(String fileUrl, String savePath);
}
