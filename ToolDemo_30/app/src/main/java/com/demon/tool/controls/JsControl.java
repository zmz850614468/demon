package com.demon.tool.controls;


import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * 与Js通信用对象
 */
public class JsControl {


    private WebView webView;
//    private MainActivity activity;

//    public JsControl(WebView webView, MainActivity activity) {
//        this.webView = webView;
//        this.activity = activity;
//    }

    //  TODO 调用js端已注册的函数
    public void hadrwareCallBack(int type, float data) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("javascript:Hardware_CallBack(")
                .append(type).append(",")
                .append(data)
                .append(")");

        webView.loadUrl(buffer.toString());
    }

    // TODO 注册函数，供js端调用
    @JavascriptInterface    // 开启摄像头，并设置位置
    public void Video_Open(int x, int y, int width, int height) {
//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });
    }

}
