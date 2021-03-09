package com.lilanz.wificonnect.activity_new;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.control_new.ServerOkSocketControl;

import butterknife.ButterKnife;

public class ServerActivity extends AppCompatActivity {

    private ServerOkSocketControl serverOkSocketControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ButterKnife.bind(this);

        initData();
    }
    
    private void initData(){
        serverOkSocketControl = ServerOkSocketControl.getInstance(this);
        serverOkSocketControl.beginServerSocket(8899);
    }
}
