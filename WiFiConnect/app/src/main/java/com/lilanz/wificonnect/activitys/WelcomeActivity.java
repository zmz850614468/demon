package com.lilanz.wificonnect.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.activity_new.App;
import com.lilanz.wificonnect.activity_new.HomeDeviceActivity;
import com.lilanz.wificonnect.activity_new.ServerActivity;
import com.lilanz.wificonnect.controls.AppDataControl;
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
        toActivity(become);
        requestPermissions();
    }

    @OnClick({R.id.tv_be_service, R.id.tv_be_operate, R.id.tv_control_device})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_be_service:
                SharePreferencesUtil.saveBecome(this, "服务端");
                toActivity("服务端");
                break;
            case R.id.tv_be_operate:
                SharePreferencesUtil.saveBecome(this, "客户端");
                toActivity("客户端");
                break;
            case R.id.tv_control_device:
                SharePreferencesUtil.saveBecome(this, "直接控制设备");
                toActivity("直接控制设备");
                break;
        }
    }

    /**
     * 跳转到对应的界面
     *
     * @param to
     */
    private void toActivity(String to) {
        AppDataControl.selectedType = to;
        if ("服务端".equals(to)) {
//            Intent intent = new Intent(this, ServiceActivity.class);
            Intent intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
            finish();
        } else if ("客户端".equals(to)) {
            Intent intent = new Intent(this, OperateActivity.class);
            startActivity(intent);
            finish();
        } else if ("直接控制设备".equals(to)) {
//            Intent intent = new Intent(this, ControlDeviceActivity.class);
            Intent intent = new Intent(this, HomeDeviceActivity.class);
            startActivity(intent);
            finish();
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
