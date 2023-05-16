package com.lilanz.videoexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start();
    }

    public void start() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                ExchangeControl control = new ExchangeControl(MainActivity.this);
                control.exchange();
            }
        };
        thread.start();
    }
}
