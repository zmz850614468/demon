package com.demo.fightdata.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.demo.fightdata.R;
import com.demo.fightdata.util.NotificationUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        "file:///android_asset/test.txt"
    }

    @OnClick(R.id.tv_)
    public void onClicked(View v) {
//        NotificationUtil.sendNotification(this);
        showLog("发送通知");
    }

    private void showLog(String msg) {
        Log.e("MainActivity", msg);
    }
}
