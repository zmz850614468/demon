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
import com.lilanz.tooldemo.multiplex.bleModel.BleActivity;
import com.lilanz.tooldemo.multiplex.daos.DaoExaActivity;
import com.lilanz.tooldemo.multiplex.qrcode.CodeScanExaActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @OnClick({R.id.bt_ble, R.id.bt_scan_code, R.id.bt_dao_test, R.id.bt_camera2})
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
        }
    }
}
