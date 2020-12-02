package com.lilanz.wificonnect.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        String become = SharePreferencesUtil.getBecome(this);
        showLog("信息：" + become);
        if ("服务端".equals(become)) {
            Intent intent = new Intent(this, ServiceActivity.class);
            startActivity(intent);
            finish();
        } else if ("客户端".equals(become)) {
            Intent intent = new Intent(this, OperateActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @OnClick({R.id.tv_be_service, R.id.tv_be_operate})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_be_service:
                SharePreferencesUtil.saveBecome(this, "服务端");
                Intent intent = new Intent(this, ServiceActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_be_operate:
                SharePreferencesUtil.saveBecome(this, "客户端");
                intent = new Intent(this, OperateActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void showLog(String msg) {
        Log.e("", msg);
        if (App.isDebug) {
        }
    }
}
