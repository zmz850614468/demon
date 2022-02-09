package com.demon.fit.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demon.fit.R;
import com.demon.fit.activity_ui.CostUi;

import butterknife.ButterKnife;

public class CostActivity extends AppCompatActivity {

    private CostUi costUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);
        ButterKnife.bind(this);

        costUi = new CostUi(this);
    }
}
