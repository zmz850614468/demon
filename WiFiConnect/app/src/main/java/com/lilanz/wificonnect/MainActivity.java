package com.lilanz.wificonnect;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lilanz.wificonnect.threads.OnReceiverListener;
import com.lilanz.wificonnect.threads.ServerThread;
import com.lilanz.wificonnect.utils.WiFiUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btSearch;
    private Button btGetName;
    private Button btStartService;
    private Button btConnect;
    private Button btSendMsg;
    private TextView tvMsg;
    private EditText etPort;
    private EditText etIp;
    private EditText etMsg;
    private Button btClose;

    private ServerThread serverThread;
    private SocketThread socketThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private boolean canOpen() {
        if (socketThread != null) {
            showMsg("已经连接上服务端口, 无法进行此操作");
            return false;
        }
        if (serverThread != null) {
            showMsg("已经打开服务端口, 无法进行此操作");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_name:
                String ipAddr = WiFiUtil.getIpAddr(this);
                showMsg(ipAddr);
                break;
            case R.id.bt_open_service:
                if (!canOpen()) {
                    return;
                }
                try {
                    int port = Integer.parseInt(etPort.getText().toString());
                    startService(port);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMsg("端口数据错误");
                }
                break;
            case R.id.bt_start_connect:
                if (!canOpen()) {
                    return;
                }
                try {
                    int port = Integer.parseInt(etPort.getText().toString());
                    String ip = etIp.getText().toString();
                    startConnect("192.168.1." + ip, port);
//                    startConnect("103.46.128.45", 48539);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMsg("ip或端口 数据错误");
                }
                break;
            case R.id.bt_send_msg:
                String msg = etMsg.getText().toString();
                if ("".equals(msg)) {
                    showMsg("信息不能为空");
                } else {
                    sendMsg(msg);
                }
                etMsg.setText("");
                break;
            case R.id.bt_close_port:
                tvMsg.setText("");
                close();
                break;
            case R.id.bt_search_ip:
                Intent intent = new Intent(this, SearchIpActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 写数据
     *
     * @param msg
     */
    private void sendMsg(@NonNull final String msg) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (socketThread != null) {
                    socketThread.sendMsg(msg);
                }
                if (serverThread != null) {
                    serverThread.sendMsg(msg);
                }
            }
        };
        thread.start();
    }

    /**
     * 连接服务端口
     *
     * @param ip
     * @param port
     */
    private void startConnect(String ip, int port) {
        socketThread = new SocketThread();
        socketThread.setListener(new OnReceiverListener() {
            @Override
            public void onReceiver(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(msg);
                    }
                });
            }

            @Override
            public void onReceiver(SocketThread thread, final String msg) {
                super.onReceiver(thread, msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(msg);
                    }
                });
                socketThread = null;
            }
        });
        socketThread.startConnect(ip, port);
    }

    /**
     * 开启服务端口
     *
     * @param port
     */
    private void startService(int port) {
        serverThread = new ServerThread(port);
        serverThread.setListener(new OnReceiverListener() {
            @Override
            public void onReceiver(SocketThread thread, final String msg) {
                super.onReceiver(thread, msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(msg);
                    }
                });
            }

            @Override
            public void onReceiver(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(msg);
                    }
                });
            }
        });
        serverThread.start();
    }

    private void initUI() {
        btSearch = findViewById(R.id.bt_search_ip);
        btClose = findViewById(R.id.bt_close_port);
        etPort = findViewById(R.id.et_port);
        etIp = findViewById(R.id.et_ip);
        etMsg = findViewById(R.id.et_msg);
        btGetName = findViewById(R.id.bt_get_name);
        btStartService = findViewById(R.id.bt_open_service);
        btConnect = findViewById(R.id.bt_start_connect);
        btSendMsg = findViewById(R.id.bt_send_msg);
        tvMsg = findViewById(R.id.tv_msg);
        btSearch.setOnClickListener(this);
        btClose.setOnClickListener(this);
        btSendMsg.setOnClickListener(this);
        btConnect.setOnClickListener(this);
        btStartService.setOnClickListener(this);
        btGetName.setOnClickListener(this);
    }

    private void close() {
        if (socketThread != null) {
            socketThread.close();
            socketThread = null;
        }
        if (serverThread != null) {
            serverThread.close();
            serverThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        close();
        super.onDestroy();
    }

    private void showMsg(String msg) {
        tvMsg.append(msg + "\n");
    }


}
