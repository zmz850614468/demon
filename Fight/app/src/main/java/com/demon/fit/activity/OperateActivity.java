package com.demon.fit.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;

/**
 * 操作说明界面
 */
public class OperateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = null;
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        if (type == null) {
            setContentView(R.layout.activity_operate);
        }else if ("k-test".equals(type)) {
            setContentView(R.layout.activity_operate_test);
        }
    }
}
