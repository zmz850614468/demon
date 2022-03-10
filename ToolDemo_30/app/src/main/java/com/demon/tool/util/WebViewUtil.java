package com.demon.tool.util;

import android.content.Context;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class WebViewUtil {

    /**
     * 初始化webView
     *
     * @param context
     * @param webView
     */
    public static void init(Context context, WebView webView) {
        String appCachePath = context.getCacheDir().getAbsolutePath();
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        //用于js调用Android
        webSettings.setJavaScriptEnabled(true);
        //设置编码方式
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setAppCachePath(appCachePath);

        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setSupportZoom(true);
    }

    /**
     * 注册webView函数
     */
    private void register() {
//        JsControl jsControl = new JsControl(webView, this);
//        webView.addJavascriptInterface(jsControl, "_JIAndroidObj");
    }
}
