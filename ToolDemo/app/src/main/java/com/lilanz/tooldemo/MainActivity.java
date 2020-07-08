package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.API.APIRequest;
import com.lilanz.tooldemo.API.ParseListener;
import com.lilanz.tooldemo.multiplex.activitys.ReuseActivity;
import com.lilanz.tooldemo.utils.StringUtil;
import com.lilanz.tooldemo.utils.internetcheck.InternetCheckUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        showMsg(StringUtil.getTimeStr(68));
    }

    @OnClick({R.id.bt_internal_check, R.id.bt_reuse})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reuse:
                Intent intent = new Intent(this, ReuseActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_internal_check:
                InternetCheckUtil.internetCheck(this, "webt.lilang.com");//"webt.lilang.com" ; "www.baidu.com"
                break;
        }
    }

    private void requestTest() {
        APIRequest<Nullable> request = new APIRequest<>(Nullable.class);
        request.setParseListener(new ParseListener<Nullable>() {
            @Override
            public void onTip(String msg) {
                showToast(msg);
            }

            @Override
            public void onError(String msg) {
                showToast(msg);
            }
        });
        request.requestFor(new HashMap<String, Object>(), "getNumberList", APIRequest.PARSE_TYPE_NULL);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

    private void test() {
        boolean b = false;
    }
}


