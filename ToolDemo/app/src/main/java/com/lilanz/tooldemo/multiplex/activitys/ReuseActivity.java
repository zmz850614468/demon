package com.lilanz.tooldemo.multiplex.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lilanz.tooldemo.R;
import com.lilanz.tooldemo.multiplex.camera2.Camera2ExaActivity;
import com.lilanz.tooldemo.multiplex.API.APIActivity;
import com.lilanz.tooldemo.multiplex.bleModel.BleActivity;
import com.lilanz.tooldemo.multiplex.daos.DaoExaActivity;
import com.lilanz.tooldemo.multiplex.camera1.CameraActivity;
import com.lilanz.tooldemo.multiplex.download.DownloadActivity;
import com.lilanz.tooldemo.multiplex.qrcode.CodeScanExaActivity;
import com.lilanz.tooldemo.multiplex.scanhelper.ScanActivity;
import com.lilanz.tooldemo.multiplex.web.WebActivity;
import com.lilanz.tooldemo.multiplex.websocket.WebSocketActivity;
import com.lilanz.tooldemo.multiplex.wificonnect.WifiConnectActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 各种复用例子集合类
 */
public class ReuseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reuse);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_ble, R.id.bt_scan_code, R.id.bt_dao_test, R.id.bt_camera2,
            R.id.bt_api_request, R.id.bt_wifi_connect, R.id.bt_webview,
            R.id.bt_scan, R.id.bt_down, R.id.bt_web_socket, R.id.bt_camera1})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_ble:           // 蓝牙例子
                Intent intent = new Intent(this, BleActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_scan_code:     // 扫描例子
                intent = new Intent(this, CodeScanExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_dao_test:      // 数据库例子
                intent = new Intent(this, DaoExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_camera2:       // 相机例子
                intent = new Intent(this, Camera2ExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_camera1:
                intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_api_request:
                intent = new Intent(this, APIActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_wifi_connect:
                intent = new Intent(this, WifiConnectActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_webview:
                intent = new Intent(this, WebActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_scan:
                intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_down:
                intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_web_socket:
                intent = new Intent(this, WebSocketActivity.class);
                startActivity(intent);
                break;
        }
    }
}
