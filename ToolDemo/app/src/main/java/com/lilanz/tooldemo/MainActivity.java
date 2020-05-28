package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilanz.tooldemo.camera2.Camera2ExaActivity;
import com.lilanz.tooldemo.qrcode.CodeScanExaActivity;
import com.lilanz.tooldemo.utils.internetcheck.InternetCheckUtil;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btScanCode;
    private Button btCamera2;
    private Button btInternalCheck;
    private Button btCheckUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera2:
                Intent intent = new Intent(this, Camera2ExaActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_internal_check:
                InternetCheckUtil.internetCheck(this, "webt.lilang.com");//"webt.lilang.com" ; "www.baidu.com"
                break;
            case R.id.bt_scan_code:
                intent = new Intent(this, CodeScanExaActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initUI() {
        btScanCode = findViewById(R.id.bt_scan_code);
        btCamera2 = findViewById(R.id.bt_camera2);
        btInternalCheck = findViewById(R.id.bt_internal_check);
        btCheckUpdate = findViewById(R.id.bt_);
        btScanCode.setOnClickListener(this);
        btCheckUpdate.setOnClickListener(this);
        btInternalCheck.setOnClickListener(this);
        btCamera2.setOnClickListener(this);
    }
}
