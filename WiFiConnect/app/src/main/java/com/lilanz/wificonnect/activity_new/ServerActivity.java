package com.lilanz.wificonnect.activity_new;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.control_new.ServerOkSocketControl;
import com.xuhao.didi.socket.common.interfaces.common_interfacies.server.IClient;

import butterknife.ButterKnife;

public class ServerActivity extends AppCompatActivity {

    private ServerOkSocketControl serverOkSocketControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ButterKnife.bind(this);

        initService();
    }

    private void initService() {
        serverOkSocketControl = ServerOkSocketControl.getInstance(this);
        serverOkSocketControl.beginServerSocket(8899);
        serverOkSocketControl.addListener(new ServerOkSocketControl.OnSocketListener() {
            @Override
            public void onMsgCallback(IClient client, String ip, byte[] msg) {
                showLog(ip + ":" + new String(msg));
            }

            @Override
            public void onDisconnect(IClient client, String ip) {

            }
        });
    }

    private void showLog(String msg) {
        if (App.isDebug) {
            Log.e("serverActivity", msg);
        }
    }
}
