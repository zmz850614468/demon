package com.demon.fit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.demon.fit.activity.CalculateActivity;
import com.demon.fit.activity.MoneyTargetActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_target, R.id.bt_ful})
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.bt_target:
                Intent intent = new Intent(this, MoneyTargetActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_ful:
                intent = new Intent(this, CalculateActivity.class);
                startActivity(intent);
                break;
        }
    }
}
