package com.lilanz.tooldemo.multiplex.wificonnect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lilanz.tooldemo.R;

import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.IO;
import io.socket.client.Socket;

public class WifiConnectActivity extends Activity {

    @BindView(R.id.tv_msg)
    protected TextView tvMsg;

    @BindView(R.id.et_port)
    protected EditText etPort;
    @BindView(R.id.et_ip)
    protected EditText etIp;

    private Socket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connect);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_ip, R.id.bt_search_ip})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_search_ip:
                Intent intent = new Intent(this, SearchIpActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_ip:
                String ipAddr = WiFiUtil.getIpAddr(this);
                showMsg(ipAddr);
                break;
        }
    }

    /**
     * 初始化socket服务器
     */
    private void initSocketServer() {
        String ip = etIp.getText().toString();
        String port = etPort.getText().toString();
        try {
            socket = IO.socket("192.168.1." + ip + ":" + port);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            showToast("初始化socket异常");
            return;
        }
        socket.on("msg", args -> {
            receiverWifiMsg((JSONObject) args[0]);
        });

    }

    public void receiverWifiMsg(JSONObject jsonObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(jsonObject.toString());
            }
        });
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
