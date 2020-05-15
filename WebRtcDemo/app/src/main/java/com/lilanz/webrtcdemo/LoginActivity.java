package com.lilanz.webrtcdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import com.lilanz.webrtcdemo.remove.RemovePreviewActivity;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button btLocal;
    private Button btRemove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        request();
        initUI();
    }

    private void request() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO}, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_local:
                Intent intent = new Intent(this, LocalPreviewActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_remove:
                intent = new Intent(this, RemovePreviewActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initUI() {
        btLocal = findViewById(R.id.bt_local);
        btRemove = findViewById(R.id.bt_remove);
        btLocal.setOnClickListener(this);
        btRemove.setOnClickListener(this);
    }
}
