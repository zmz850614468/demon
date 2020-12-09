package com.lilanz.wificonnect.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
        requestPermissions();
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

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE, Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLog(String msg) {
        Log.e("", msg);
        if (App.isDebug) {
        }
    }
}
