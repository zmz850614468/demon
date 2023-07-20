package com.demon.fit.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.activity_ui.DrawUi;

import butterknife.ButterKnife;

public class DrawActivity extends AppCompatActivity {

    private DrawUi drawUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        drawUi = new DrawUi(this);
    }
}
