package com.lilanz.tooldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lilanz.tooldemo.camera2.Camera2ExaActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btCamera2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_camera2:
                Intent intent = new Intent(this, Camera2ExaActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initUI() {
        btCamera2 = findViewById(R.id.bt_camera2);
        btCamera2.setOnClickListener(this);
    }
}
