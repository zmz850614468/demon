package com.lilanz.kotlintool.utils

import android.content.Context
import android.webkit.WebView

class WebViewUtil {
    companion object {
        /**
         * 初始化webview
         */
        fun init(context: Context, webView: WebView) {
            val appCachePath = context.cacheDir.absolutePath
            val webSettings = webView.settings
            webSettings.allowUniversalAccessFromFileURLs = true
            webSettings.allowFileAccess = true
            webSettings.allowFileAccessFromFileURLs = true
            //用于js调用Android
            webSettings.javaScriptEnabled = true
            //设置编码方式
            webSettings.defaultTextEncodingName = "utf-8"
            webSettings.domStorageEnabled = true
            webSettings.setAppCacheMaxSize(1024 * 1024 * 8.toLong())
            webSettings.setAppCachePath(appCachePath)

            webSettings.allowFileAccess = true
            webSettings.setAppCacheEnabled(true)

            webSettings.allowContentAccess = true
            webSettings.builtInZoomControls = true
            webSettings.displayZoomControls = false

            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.setGeolocationEnabled(true)
            webSettings.databaseEnabled = true
            webSettings.setSupportZoom(true)
        }
    }
}