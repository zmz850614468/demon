package com.demon.fit.data_transfer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.demon.fit.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 不同设备间，通过webSocket进行数据迁移
 */
public class DataTransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_send, R.id.bt_receive})
    public void onClicked(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.bt_receive:
                intent = new Intent(this, ReceiverDeviceActivity.class);
                break;
            case R.id.bt_send:
                intent = new Intent(this, SendDeviceActivity.class);
                break;
        }
        startActivity(intent);
    }
}
