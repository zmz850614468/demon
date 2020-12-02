package com.example.timeup;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_update_check)
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_update_check:
                Beta.checkUpgrade();
                break;
        }
    }


}
