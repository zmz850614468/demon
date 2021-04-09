package com.demon.tool.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.demon.tool.R;
import com.demon.tool.controls.PermissionControl;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPermission();
    }

    @OnClick(R.id.bt_camera)
    public void onClicked(View v) {
        switch (v.getId()) {
        }
    }

    private void initPermission() {
        PermissionControl control = new PermissionControl(this);
        control.storagePermission();
        control.cameraPermission();
    }
}
