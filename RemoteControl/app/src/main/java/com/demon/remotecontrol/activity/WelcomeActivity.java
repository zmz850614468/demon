package com.demon.remotecontrol.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.remotecontrol.R;
import com.demon.remotecontrol.control.PermissionControl;
import com.demon.remotecontrol.util.RootUtil;
import com.demon.remotecontrol.util.SharePreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    public static final String DEVICE_TYPE_REMOTE = "远端设备";
    public static final String DEVICE_TYPE_CONTROL = "控制设备";

    @BindView(R.id.bt_remote)
    Button btRemote;

    @BindView(R.id.bt_control)
    Button btControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);


        if (RootUtil.isRoot(this)) {
            onClicked(btRemote);
            return;
        }

//        onClicked(btRemote);

        String type = SharePreferencesUtil.getDeviceType(this);
        switch (type) {
            case DEVICE_TYPE_REMOTE:
                onClicked(btRemote);
                break;
            case DEVICE_TYPE_CONTROL:
                onClicked(btControl);
                break;
        }
    }

    @OnClick({R.id.bt_remote, R.id.bt_control})
    public void onClicked(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_remote:
                SharePreferencesUtil.saveDeviceType(this, DEVICE_TYPE_REMOTE);
                intent = new Intent(this, RemoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.bt_control:
                SharePreferencesUtil.saveDeviceType(this, DEVICE_TYPE_CONTROL);
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
