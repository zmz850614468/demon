package com.demon.fit.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.activity_ui.OperateResultUi;

import butterknife.ButterKnife;

/**
 * 新的操作结果界面
 */
public class OperateResultActivity extends AppCompatActivity {

    private OperateResultUi operateResultUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate_result);
        ButterKnife.bind(this);

        operateResultUi = new OperateResultUi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        operateResultUi.updateData();
    }
}
