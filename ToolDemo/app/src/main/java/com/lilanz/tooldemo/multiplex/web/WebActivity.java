package com.lilanz.tooldemo.multiplex.web;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.utils.WebViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends Activity {

    @BindView(R.id.webview)
    protected WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {
        WebViewUtil.init(this, webView);
        webView.loadUrl("file:///android_asset/normal.html");
    }

}
