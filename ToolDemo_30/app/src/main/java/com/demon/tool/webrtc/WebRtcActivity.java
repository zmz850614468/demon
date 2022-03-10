package com.demon.tool.webrtc;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.tool.R;
import com.demon.tool.util.WebViewUtil;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebRtcActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webrtc);
        ButterKnife.bind(this);

        init();
    }


    private void init() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setPluginsEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        WebViewUtil.init(this, webView);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showLog("授权");
                        request.grant(request.getResources());
                    }
                });
            }
        });

//        webView.loadUrl("https://www.baidu.com/");
        showLog("加载webRtc地址");
//        webView.loadUrl("https://192.168.35.174:9988/room.php?cid=123");
        webView.loadUrl("https://apprtc.webrtcserver.cn/");
//        webView.loadUrl("https://meet.lilanz.com:12580/");

    }

    private void showLog(String msg) {
        Log.e("WebRtcActivity", msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
