package com.demon.agv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.demon.agv.R;
import com.demon.agv.control.PermissionControl;
import com.demon.agv.util.PublicFileUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new PermissionControl(this).storagePermission();
        new PermissionControl(this).locationPermission();
    }


    @OnClick(R.id.tv_agv)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_agv:
                int a = 10 / 0;
//                PublicFileUtil.saveTxt2Public(this, Environment.DIRECTORY_DOCUMENTS,
//                        "test.txt", "test content!\r\n", "wa");
//                String content = PublicFileUtil.getTextFromPublic(this, Environment.DIRECTORY_DOCUMENTS,
//                        "test.txt");
//                showLog(content);
                break;
        }
    }

    private void showLog(String msg) {
        Log.e("main", msg);
    }
}
