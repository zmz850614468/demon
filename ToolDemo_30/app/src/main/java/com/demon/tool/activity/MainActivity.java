package com.demon.tool.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.demon.tool.R;
import com.demon.tool.controls.PermissionControl;
import com.demon.tool.controls.ScanKeyManager;
import com.demon.tool.view.CustomToast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    private ScanKeyManager scanKeyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPermission();
        initScanCode();
    }

    @OnClick(R.id.bt_camera)
    public void onClicked(View v) {
        switch (v.getId()) {
        }
    }

    /**
     * 处理设备输入问题
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // deviceId>0 的是设备 ； ==-1的是平板输入
        if (event.getDeviceId() > 0 && event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
            scanKeyManager.analysisKeyEvent(event);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initScanCode(){
        //拦截扫码器回调,获取扫码内容
        scanKeyManager = new ScanKeyManager(new ScanKeyManager.OnScanValueListener() {
            @Override
            public void onScanValue(String value) {
                showLog("code:" + value);
            }
        });
    }


    private void initPermission() {
        PermissionControl control = new PermissionControl(this);
        control.storagePermission();
        control.cameraPermission();
    }

    private void showLog(String msg){
            Log.e("MainActivity", msg);
    }
}
