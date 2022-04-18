package com.demon.module.model;

import android.util.Log;

/**
 * 网络请求类
 */
public class HttpModel {

    public void requestData(String params) {
        showLog("HttpModel : " + params);
    }

    private void showLog(String msg) {
        Log.e("HttpControl", msg);
    }
}
