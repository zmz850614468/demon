package com.lilanz.wificonnect.activitys;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lilanz.wificonnect.R;
import com.lilanz.wificonnect.beans.MsgBean;
import com.lilanz.wificonnect.controls.PermissionControl;
import com.lilanz.wificonnect.threads.WifiService;
import com.lilanz.wificonnect.utils.SharePreferencesUtil;
import com.lilanz.wificonnect.utils.WiFiUtil;
import com.tencent.bugly.Bugly;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceActivity extends AppCompatActivity {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.tv_msg)
    protected TextView tvMsg;
    @BindView(R.id.et_msg)
    protected EditText etMsg;

    private WifiService wifiService;
    private PermissionControl permissionControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Bugly.init(this, "1fa246be97", true);
        initUI();
        permissionControl = new PermissionControl(this);
        permissionControl.storagePermission();

        initUI();
        initService();
    }

    @OnClick(R.id.iv_setting)
    public void onSettingClicked() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_refresh)
    public void onRefreshClicked() {
        tvMsg.setText("");
        ivRefresh.setEnabled(false);
        if (wifiService != null) {
            wifiService.close();
            wifiService.startService(SharePreferencesUtil.getServicePort(this));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivRefresh.setEnabled(true);
            }
        }, 1000);
    }

    private void initService() {
        Intent intent = new Intent(this, WifiService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wifiService = ((WifiService.WifiBind) service).getService();
            wifiService.setListener(new WifiService.OnDataCallbackListener() {
                @Override
                public void onCallBack(int type, String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (type) {
                                case 0:
                                    showMsg(msg);
                                    break;
                                case 3:
                                    tvStatus.setText("连接状态");
                                    tvStatus.setTextColor(getResources().getColor(R.color.black));
                                    break;
                                case 4:
                                    tvStatus.setText("断开状态");
                                    tvStatus.setTextColor(getResources().getColor(R.color.red));
                                    break;
                            }
                        }
                    });
                }
            });
            onRefreshClicked();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wifiService = null;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        showMsg(requestCode + " : " + new Gson().toJson(permissions) + " = " + new Gson().toJson(grantResults));
    }


    @OnClick(R.id.tv_send_msg)
    public void onSendMsgClicked(View v) {
        String msg = etMsg.getText().toString();
        if ("".equals(msg)) {
            showMsg("信息不能为空");
        } else {
            if (wifiService != null) {
                wifiService.sendMsg(new MsgBean(2, msg).toString());
            }
        }
        etMsg.setText("");
    }

    private void initUI() {
        String ipAddr = WiFiUtil.getIpAddr(this);
        tvAddress.setText("ip:" + ipAddr);
    }

    @Override
    protected void onDestroy() {
        if (wifiService != null) {
            unbindService(connection);
            wifiService = null;
        }
        super.onDestroy();
    }

    private void showMsg(String msg) {
        if (tvMsg.length() > 100) {
            tvMsg.setText(msg + "\n");
        } else {
            tvMsg.append(msg + "\n");
        }
    }

}
